# 배포 절차 (수동 배포)

> 작성 기준: 2026-10-08 / 대상: JSP 게시판(war)  
> GitHub Actions 자동 배포를 붙이면 이 문서는 "비상시 수동 배포"로 바뀝니다.

## 환경 요약

| 항목 | 값 |
|---|---|
| 서버 | EC2 t3.micro / Amazon Linux 2023 / 서울 리전 |
| 런타임 | Amazon Corretto 17 |
| 앱 위치 | `/home/ec2-user/app.war` |
| 앱 실행 | systemd 서비스 `spring-playground` (내부 포트 9090) |
| 외부 진입 | Nginx 80 → 127.0.0.1:9090 |
| 설정값·비밀번호 | `/etc/spring-playground.env` (권한 600) |
| DB | RDS MySQL 8.0 (프라이빗) / DB명 `playground` |

자리표시자: `<EC2_PUBLIC_IP>` `<KEY_PATH>` `<RDS_ENDPOINT>`  
실제 값은 저장소 밖 개인 메모에 둡니다.

## 사전 준비 (PC 최초 1회)

1. JDK 17 준비: Amazon Corretto 17 Windows x64 zip을 받아 압축 해제  
   (msi 설치는 시스템 JAVA_HOME을 바꿀 수 있어서 zip 사용)
2. `build17.bat.example`을 `build17.bat`으로 복사하고 JDK 경로 수정
3. `build17.bat`은 `.gitignore`에 포함되어 커밋되지 않음

업무용 Java 8 환경을 건드리지 않도록 JAVA_HOME은 cmd 창 안에서만 바꿉니다.

## 소스 수정 후 배포

### 1. 빌드 (PC)
```bat
build17.bat
```
`BUILD SUCCESS` 후 `target\spring-playground-0.0.1-SNAPSHOT.war` 생성 확인

### 2. 업로드 (PC)
```bat
scp -i <KEY_PATH> target\spring-playground-0.0.1-SNAPSHOT.war ec2-user@<EC2_PUBLIC_IP>:~/app.war
```
- EC2 재시작으로 IP가 바뀌면 지문 확인 질문이 다시 뜸 → `-o StrictHostKeyChecking=accept-new` 추가
- `Connection timed out`이면 보안 그룹 SSH 규칙의 "내 IP"를 현재 IP로 갱신

### 3. 재시작 (EC2)
```bash
sudo systemctl restart spring-playground
journalctl -u spring-playground -f
```
`Started SpringPlaygroundApplication`이 나올 때까지 확인 후 `Ctrl + C`  
t3.micro 기준 기동에 20~30초 걸리며 그동안 브라우저는 502가 정상

### 4. 확인
- 브라우저: `http://<EC2_PUBLIC_IP>`
- 서버 내부: `curl -I localhost:9090`

## DB 스키마를 바꾼 경우

JPA `ddl-auto` 설정이 없어서 테이블이 자동으로 바뀌지 않습니다.  
**로컬 MySQL과 RDS 둘 다** 같은 SQL을 실행해야 합니다.

- RDS 실행 방법 A: DBeaver SSH 터널로 접속 후 실행 (infra.md 참고)
- RDS 실행 방법 B: EC2에서
  ```bash
  mysql -h <RDS_ENDPOINT> -u admin -p playground
  ```
- 실행한 SQL은 `docs/sql/` 아래에 날짜별로 남겨둡니다.

## 설정값(비밀번호 등)을 바꾼 경우

소스 재배포 없이 서버에서만 바꿉니다.
```bash
sudo vi /etc/spring-playground.env
sudo systemctl restart spring-playground
```
Spring Boot는 환경 변수가 yml 값을 덮어씁니다.  
예: `spring.datasource.url` → `SPRING_DATASOURCE_URL`

현재 사용하는 변수:
```
SPRING_DATASOURCE_URL=jdbc:mysql://<RDS_ENDPOINT>:3306/playground
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=
SPRING_MAIL_PASSWORD=
```

## 자주 쓰는 명령

| 목적 | 명령 |
|---|---|
| 앱 상태 | `sudo systemctl status spring-playground` |
| 앱 재시작 | `sudo systemctl restart spring-playground` |
| 앱 최근 로그 | `journalctl -u spring-playground -n 50 --no-pager` |
| 앱 실시간 로그 | `journalctl -u spring-playground -f` |
| Nginx 설정 검사 | `sudo nginx -t` |
| Nginx 재시작 | `sudo systemctl restart nginx` |

앱은 터미널에서 `java -jar`로 직접 실행하지 않습니다.  
환경 변수가 없는 상태로 떠서 `localhost:3306`에 접속하려다 실패합니다.

## 실습 종료 시 (비용)

1. EC2 → 인스턴스 중지 (다시 켜면 퍼블릭 IP 변경)
2. RDS → 일시 중지 (7일 후 자동으로 다시 켜짐)
3. 예산 알림 메일 확인
