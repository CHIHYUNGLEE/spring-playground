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

### 로컬 실행용 환경 변수 (이클립스)

`application.yml`에는 비밀번호를 적지 않습니다. 로컬에서도 환경 변수로 넣어야 기동됩니다.

1. Run → Run Configurations... → `SpringPlaygroundApplication` 선택  
   (Spring Boot App 또는 Java Application 아래. 없으면 새로 만들고 Main type에 `com.chihyunglee.springplayground.SpringPlaygroundApplication`)
2. Environment 탭 → Add...

   | Name | Value |
   |---|---|
   | `SPRING_DATASOURCE_PASSWORD` | 로컬 MySQL 비밀번호 (필수) |
   | `SPRING_MAIL_PASSWORD` | 구글 앱 비밀번호 (메일 기능 안 쓰면 생략) |

3. "Append environment to native environment" 유지 → Apply → Run

- Common 탭의 "Shared file"은 체크하지 않습니다. 체크하면 비밀번호가 든 `.launch` 파일이 프로젝트 안에 생겨 커밋될 수 있습니다.
- 이클립스 밖(PowerShell)에서 실행할 때: `$env:SPRING_DATASOURCE_PASSWORD='...'` 후 `java -jar` 또는 `mvn spring-boot:run`
- DB URL·계정은 yml 기본값(`localhost:3306/spring_playground`, `myuser`)을 쓰므로 로컬에서는 넣지 않아도 됩니다.

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

### 동작 구조

```
/etc/spring-playground.env  (root 소유, 600)
        │  EnvironmentFile=
        ▼
spring-playground.service (User=ec2-user, java -jar /home/ec2-user/app.war)
        ▼
application.yml 의 ${SPRING_...} 자리에 값이 들어감
```

### 사용하는 변수

```
SPRING_DATASOURCE_URL=jdbc:mysql://<RDS_ENDPOINT>:3306/playground
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=
SPRING_MAIL_PASSWORD=
```

| 변수 | 필수 | 없을 때 |
|---|---|---|
| `SPRING_DATASOURCE_PASSWORD` | 필수 (yml 기본값 없음) | 앱이 기동하지 않음 |
| `SPRING_DATASOURCE_URL` | 운영 필수 | yml 기본값(localhost)으로 접속하려다 실패 |
| `SPRING_DATASOURCE_USERNAME` | 운영 필수 | yml 기본값(`myuser`)으로 접속하려다 실패 |
| `SPRING_MAIL_PASSWORD` | 선택 | 앱은 뜨지만 비밀번호 찾기 메일 발송 실패 |

작성 규칙
- 한 줄에 `KEY=VALUE` 하나. `=` 앞뒤 공백 없이, `export` 붙이지 않음
- 구글 앱 비밀번호는 화면에 `abcd efgh ijkl mnop`처럼 보이지만 **공백을 빼고 16자 그대로** 입력

### 권한

```bash
sudo chown root:root /etc/spring-playground.env
sudo chmod 600 /etc/spring-playground.env
ls -l /etc/spring-playground.env   # -rw------- 1 root root
```
systemd가 root로 파일을 읽은 뒤 ec2-user로 앱을 실행하므로 ec2-user에게 읽기 권한이 없어도 됩니다.

### 적용 확인 (값은 출력하지 않고 이름만)

```bash
systemctl show spring-playground -p EnvironmentFiles
sudo cat /proc/$(systemctl show -p MainPID --value spring-playground)/environ | tr '\0' '\n' | cut -d= -f1 | grep SPRING_
```

### 서비스 파일 (새로 만들 때)

```ini
# /etc/systemd/system/spring-playground.service
[Unit]
Description=spring-playground
After=network.target

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user
EnvironmentFile=/etc/spring-playground.env
ExecStart=/usr/bin/java -jar /home/ec2-user/app.war
SuccessExitStatus=143
Restart=on-failure

[Install]
WantedBy=multi-user.target
```
```bash
sudo systemctl daemon-reload            # 서비스 파일을 바꿀 때마다
sudo systemctl enable --now spring-playground
```

### 문제가 생기면

| 증상 | 원인 |
|---|---|
| 로그에 `Access denied for user ...` | `SPRING_DATASOURCE_PASSWORD`가 없거나 틀림 (변수가 없으면 `${SPRING_DATASOURCE_PASSWORD}` 글자 그대로 비밀번호로 쓰여서 이 에러가 남) |
| `Communications link failure` | `SPRING_DATASOURCE_URL` 오류 또는 RDS 중지 상태 |
| `systemctl status`의 `since`가 계속 바뀜 | 기동 실패 후 `Restart=on-failure`로 재시작 반복. `journalctl -u spring-playground -n 50 --no-pager`로 원인 확인 |
| 메일만 안 감 | `SPRING_MAIL_PASSWORD` 비었음 / 공백 포함 / 폐기된 예전 값 |

### 구글 앱 비밀번호 교체

예전 앱 비밀번호는 git 기록에 남아 있으므로 폐기하고 새로 발급해서 씁니다.
1. Google 계정 → 보안 → 앱 비밀번호 → 기존 비밀번호 삭제, 새로 생성
2. 서버 `/etc/spring-playground.env`의 `SPRING_MAIL_PASSWORD`와 로컬 이클립스 Run Configuration 둘 다 교체
3. `sudo systemctl restart spring-playground`

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
env 파일을 읽지 않아 DB 비밀번호가 없고, DB 주소도 기본값 `localhost:3306`이라 기동에 실패합니다.

## 실습 종료 시 (비용)

1. EC2 → 인스턴스 중지 (다시 켜면 퍼블릭 IP 변경)
2. RDS → 일시 중지 (7일 후 자동으로 다시 켜짐)
3. 예산 알림 메일 확인
