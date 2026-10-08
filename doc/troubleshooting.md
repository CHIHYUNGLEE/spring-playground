# 트러블슈팅 기록

> 작성 기준: 2026-10-08 / 형식: 증상 → 원인 → 해결

---

## 계정·접속

### 1. 루트 계정 MFA 분실
- **증상**: 비밀번호는 맞는데 MFA 코드를 모름
- **해결**: MFA 화면 "MFA 문제 해결" → 대체 인증 요소 → 가입 이메일 링크 + 등록 전화번호 자동 전화 코드
- **재발 방지**: 새 MFA 등록 후 분실 기기 삭제. MFA를 2개(앱 + 패스키) 등록

### 2. EC2 Instance Connect 접속 실패
- **증상**: `Error establishing SSH connection to your instance`
- **원인**: SSH 소스가 "내 IP"뿐인데 Instance Connect는 AWS 서버가 대신 접속해서 출발지 IP가 다름
- **해결**: 인바운드에 SSH(22) + 소스 `com.amazonaws.ap-northeast-2.ec2-instance-connect` 접두사 목록 추가
- **주의**: 처음에 유형을 "사용자 지정 TCP"로 넣어 포트가 22가 아니었음 → 유형을 **SSH**로 지정해야 22번이 자동 설정됨. ipv6 접두사 목록과도 헷갈리지 말 것

### 3. SSM Session Manager "연결되지 않음"
- **원인**: IAM Role을 EC2 생성 후에 붙여서 SSM 에이전트가 권한을 아직 못 읽음
- **해결**: `sudo systemctl restart amazon-ssm-agent` 후 2~5분 대기
- **참고**: 접속 사용자가 `ssm-user`라 `sudo su - ec2-user`로 전환

---

## 빌드

### 4. Maven `invalid flag: --release`
- **원인**: Maven이 Java 8로 실행됨 (업무 환경 JAVA_HOME). pom은 Java 17 컴파일 요구
- **확인**: `mvn -v` → `Java version: 1.8`
- **해결**: Corretto 17 zip 설치 후 cmd 창 안에서만 `set JAVA_HOME` → `build17.bat`으로 고정
- **포인트**: 시스템 환경변수를 바꾸지 않아 업무용 Java 8 프로젝트에 영향 없음

---

## 파일 전송 (scp)

### 5. 지문 확인 질문에 yes가 안 쳐짐
- **증상**: `The authenticity of host ... can't be established` 후 입력이 한글로만 됨
- **원인**: cmd 입력기 문제 / IP가 바뀔 때마다 새 서버로 인식
- **해결**: `scp -o StrictHostKeyChecking=accept-new ...`  
  처음 보는 서버만 자동 저장하고 지문이 바뀐 서버는 계속 막아서 `no`보다 안전

### 6. `UNPROTECTED PRIVATE KEY FILE`
- **원인**: Windows에서 키 파일에 다른 사용자 권한이 열려 있음. OpenSSH가 거부
- **해결**:
  ```bat
  icacls <KEY_PATH> /inheritance:r
  icacls <KEY_PATH> /grant:r "%USERNAME%:R"
  icacls <KEY_PATH> /remove "NT AUTHORITY\Authenticated Users" "BUILTIN\Users"
  ```
  D: 드라이브는 상속을 끊어도 명시적 권한이 남아서 세 번째 줄까지 필요  
  최종 권한: 본인(R) + Administrators + SYSTEM
- **대안**: 키를 `C:\Users\<사용자>\.ssh\`로 옮기면 권한 문제가 거의 없음

---

## DB 이전

### 7. `mysqldump` 명령을 찾을 수 없음
- **해결**: 전체 경로로 실행  
  `"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldump.exe"`
- **위치 찾기**: `where /r "C:\Program Files" mysqldump.exe`

### 8. mysqldump가 엉뚱한 DB를 뽑음 / 접근 거부
- **증상 1**: 덤프에 `columns_priv` 등 MySQL 시스템 테이블이 들어 있음
- **증상 2**: `Access denied ... to database '1234'`
- **원인**: `-p 1234`처럼 띄어 써서 비밀번호가 DB 이름으로 읽힘
- **증상 3**: `you need the PROCESS privilege`
- **해결**:
  ```bat
  mysqldump.exe -u myuser -p --no-tablespaces spring_playground > dump.sql
  findstr "CREATE TABLE" dump.sql
  ```
  비밀번호는 `-p` 뒤에 붙이지 않고 프롬프트로 입력. 덤프 후 테이블 목록 확인

### 9. RDS 적재 시 `ERROR 1046 No database selected`
- **원인**: DB 하나만 덤프하면 파일에 `USE` 문이 없음
- **해결**: 대상 DB를 명령에 지정
  ```bash
  mysql -h <RDS_ENDPOINT> -u admin -p playground < dump.sql
  ```

### 10. MariaDB 클라이언트 SSL 옵션 오류
- **증상**: `unknown variable 'ssl-mode=VERIFY_IDENTITY'` / `TLS/SSL error (2)`
- **원인**: RDS 콘솔 안내는 MySQL 공식 클라이언트용. EC2엔 MariaDB 클라이언트 설치됨. `(2)`는 pem 파일 없음 (ssm-user 폴더에서 실행)
- **해결**: SSL 없이는 기존 명령 그대로. SSL 사용 시
  ```bash
  mysql -h <RDS_ENDPOINT> -u admin -p --ssl-ca=/home/ec2-user/global-bundle.pem --ssl-verify-server-cert playground
  ```

### 11. DBeaver `Public Key Retrieval is not allowed`
- **해결**: Driver properties → `allowPublicKeyRetrieval=true`

---

## 앱 실행

### 12. `Communications link failure` / `Connection refused`
- **원인**: 환경 변수 없이 `java -jar`를 새 터미널에서 직접 실행 → yml 기본값 `localhost:3306` 사용
- **구분법**: RDS 보안 그룹 문제면 `timed out`. 즉시 `refused`면 대상 주소에 DB가 없는 것
- **해결**: 앱은 systemd로만 실행 (환경 변수 파일을 읽음)

### 13. 재시작 직후 Nginx 502
- **원인**: Spring Boot 기동 중 (t3.micro 20~30초)
- **확인**: `journalctl -u spring-playground -f`에서 `Started` 대기
- **주의**: `Restart=on-failure`라 실패해도 재시작을 반복함. `systemctl status`의 `since` 시각이 계속 바뀌면 실패 반복 중

### 14. `Could not resolve placeholder 'app.s3.region'`
- **원인**: application.yml의 `app:` 블록이 빌드 결과물에 없거나 들여쓰기가 틀림
- **확인**:
  ```bash
  unzip -p ~/app.war WEB-INF/classes/application.yml | grep -n -B1 -A3 "s3"
  ```
- **해결**: `app:`을 `spring:`과 같은 최상위 단계로. `spring:` 블록은 하나만. 저장 후 빌드

### 15. `Spring Data JDBC - Could not safely identify store assignment` (INFO)
- **원인**: pom에 data-jpa와 data-jdbc가 함께 있음. 에러 아님
- **정리 방안**: Repository가 모두 JPA면 `spring-boot-starter-data-jdbc` 제거

---

## 보안 사고 대응

### 16. 비밀번호 노출
- **사례**: Gmail 앱 비밀번호가 application.yml에 평문으로 있었음 (공개 저장소) / RDS 비밀번호를 명령줄에 입력
- **조치**: Gmail 앱 비밀번호 폐기 후 재발급 / RDS 마스터 비밀번호 변경 후 env 파일 갱신
- **원칙**:
  - 비밀번호는 yml에 쓰지 않고 서버 환경 변수로 주입
  - Git에서 파일을 지워도 커밋 이력에 남으므로 **폐기가 유일한 확실한 조치**
  - mysql `-p`는 항상 프롬프트 입력 (쉘 히스토리에 남지 않게)
