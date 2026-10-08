# AWS 인프라 구성

> 작성 기준: 2026-10-08 / 리전: ap-northeast-2 (서울)

## 구성도

```
브라우저
   │ :80
   ▼
[EC2 t3.micro] Nginx ──► Spring Boot :9090 (systemd)
   │                         │            │
   │                         │ :3306      │ IAM Role (키 없음)
   │                         ▼            ▼
   │                   [RDS MySQL]    [S3 버킷]
   │                   프라이빗        비공개 / Presigned URL
   │
   └── 관리자 접속: SSM Session Manager / Instance Connect / SSH
```

## 1. 계정·IAM

| 항목 | 구성 | 이유 |
|---|---|---|
| 루트 계정 | MFA 등록 / 액세스 키 없음 | 결제·계정 설정에만 사용 |
| 작업용 사용자 | `chihyung-admin` + MFA | 일상 작업은 루트가 아닌 IAM 사용자로 |
| 권한 부여 | `admins` 그룹에 AdministratorAccess | 사용자에 직접 붙이지 않고 그룹으로 관리 |
| 결제 조회 | IAM 사용자 결제 정보 액세스 활성화 | 루트만 켤 수 있는 설정 |
| 비용 통제 | 월 예산 알림 | RDS 등을 켜두는 사고 방지 |

## 2. EC2

| 항목 | 값 |
|---|---|
| AMI | Amazon Linux 2023 |
| 유형 | t3.micro |
| Java | java-17-amazon-corretto-headless |
| IAM Role | `spring-playground-ec2-role` |

### 보안 그룹 (인바운드)

| 유형 | 포트 | 소스 | 용도 |
|---|---|---|---|
| SSH | 22 | 내 IP | 로컬 SSH·scp·DBeaver 터널 |
| SSH | 22 | `com.amazonaws.ap-northeast-2.ec2-instance-connect` | 콘솔 Instance Connect |
| HTTP | 80 | 0.0.0.0/0 | 서비스 |

- 앱 포트 9090은 열지 않음. 외부는 Nginx 80만 노출
- Instance Connect는 내 PC가 아니라 AWS 서버가 대신 접속하므로 출발지 IP가 다름 → 접두사 목록 별도 허용

### Nginx
`/etc/nginx/conf.d/spring-playground.conf`
- `listen 80 default_server` → `proxy_pass http://127.0.0.1:9090`
- `X-Forwarded-For` `X-Forwarded-Proto` 헤더 전달
- `client_max_body_size 20M` (앱 업로드 한도 10MB보다 크게)

### systemd
`/etc/systemd/system/spring-playground.service`
- `User=ec2-user` / `WorkingDirectory=/home/ec2-user`
- `EnvironmentFile=/etc/spring-playground.env` (root 소유 600)
- `ExecStart=/usr/bin/java -jar /home/ec2-user/app.war`
- `Restart=on-failure` / `SuccessExitStatus=143`
- `enable` 상태라 서버 재부팅 시 자동 기동

## 3. RDS

| 항목 | 값 |
|---|---|
| 엔진 | MySQL 8.0 |
| 클래스 | db.t4g.micro / 단일 AZ |
| 스토리지 | 20GB / 자동 조정 끔 |
| 퍼블릭 액세스 | 아니요 |
| 초기 DB | `playground` |
| 자격 증명 | 자체 관리 (Secrets Manager 미사용: 월 요금 발생) |
| 연결 | 생성 시 "EC2 컴퓨팅 리소스에 연결" 선택 |

- RDS 보안 그룹의 3306 소스는 **IP가 아니라 EC2 보안 그룹 ID**  
  → EC2 IP가 바뀌어도 DB 접근 규칙은 유지
- 비밀번호는 yml이 아니라 서버 환경 변수로 주입
- 로컬 데이터는 `mysqldump --no-tablespaces`로 옮김

## 4. S3

| 항목 | 값 |
|---|---|
| 버킷 | `<BUCKET_NAME>` |
| 퍼블릭 액세스 | 모두 차단 |
| 암호화 | SSE-S3 (기본) |
| 객체 key | `board/<UUID>.<확장자>` |

- 원래 파일명은 DB `board_posts.file_name`에 저장. key는 UUID로 만들어 중복·한글·특수문자 문제 회피
- 다운로드는 5분 유효 Presigned URL. 서버가 파일을 중계하지 않고 버킷은 비공개 유지
- 업로드 후 DB 저장이 실패하면 S3 파일을 삭제하는 보정 처리 (S3와 DB는 한 트랜잭션이 아님)
- 게시글 삭제 시 S3 파일도 삭제

## 5. IAM Role (EC2용)

`spring-playground-ec2-role` = 아래 2개 정책

**① spring-playground-s3-access (직접 작성, 버킷 1개로 제한)**
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:PutObject", "s3:GetObject", "s3:DeleteObject"],
      "Resource": "arn:aws:s3:::<BUCKET_NAME>/*"
    },
    {
      "Effect": "Allow",
      "Action": "s3:ListBucket",
      "Resource": "arn:aws:s3:::<BUCKET_NAME>"
    }
  ]
}
```
객체 작업은 `버킷/*`에 목록 조회는 `버킷`에 권한을 줘야 함

**② AmazonSSMManagedInstanceCore (AWS 관리형)**  
Session Manager 접속용

검증 결과:
- `aws sts get-caller-identity` → `assumed-role/spring-playground-ec2-role`
- 지정 버킷 업로드·목록·삭제 성공
- `aws s3 ls` (전체 버킷 목록) → AccessDenied ✅ 최소 권한 확인

앱 코드에 액세스 키 없음. AWS SDK가 EC2 Role에서 자격 증명을 자동 획득

## 6. 접속 방법

| 대상 | 방법 | 비고 |
|---|---|---|
| EC2 | SSM Session Manager | 22번·키 불필요 / IAM으로 통제 / 이력 남음. 접속 후 `sudo su - ec2-user` |
| EC2 | Instance Connect | 콘솔에서 바로 |
| EC2 | 로컬 SSH·scp | `ssh -i <KEY_PATH> ec2-user@<EC2_PUBLIC_IP>` |
| RDS | DBeaver SSH 터널 | Main: RDS 엔드포인트 / SSH: EC2 IP + ec2-user + 키 파일 |
| RDS | EC2에서 mysql | MariaDB 클라이언트 `mariadb105` 사용 |
| RDS | SSM 포트 포워딩 | 22번을 닫는 경우의 대안 (`AWS-StartPortForwardingSessionToRemoteHost`) |

## 남은 작업
- GitHub Actions CI/CD (빌드 → 배포 자동화)
- 22번 포트 축소 검토 (배포 방식 확정 후)
- README 아키텍처 이미지
