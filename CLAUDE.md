# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Stack

Spring Boot 3.2.4 (`jakarta.*`) + Spring MVC + Spring Security + JSP/JSTL, Spring Data JPA, MySQL 8, Java 17, Maven, packaged as an executable `war` (embedded Tomcat). Not XML-configured Spring: `src/main/webapp/WEB-INF/web.xml` references a non-existent `spring-servlet.xml` and is vestigial.

## Build & run

- The machine's default Java is 8, which fails with `invalid flag: --release`. Build with `build17.bat` (sets `JAVA_HOME` to JDK 17, runs `mvn clean package -DskipTests`). Use it to verify changes compile.
- There is no Maven wrapper (`./mvnw` in README does not exist). Run locally with `mvn spring-boot:run` under JDK 17; app serves on port **9090**.
- No tests exist (`src/test` is absent).
- `vue-board/` (port 5173) and `react-board/` (port 5174) are local-only Vite frontends calling `/api/posts` on localhost:9090; they are not deployed.

## Architecture gotchas

- Two independent board implementations on different tables:
  - JSP board: `controller/*Controller` → `service/*Service` → `repository/*Repository` → `model/BoardPost` (`board_posts`), `Comment` (`board_comments`).
  - REST board for Vue/React: `board/BoardApiController` (`/api/posts`) → `service/BoardApiService` → `board/BoardApiRepository` → `board/Board` (`board`). See `doc/frontend.md`.
- Security: CSRF disabled; form login at `/login` posting to `/doLogin` with params `userId`/`password`; `/api/**` is public; BCrypt passwords.
- JSP views live in `/WEB-INF/views/`; board views use `board/usr.bbs.{list,read,write,edit}.jsp`, comments are the include fragment `board/usr.comments.jspf`.
- No `ddl-auto`: schema changes are manual SQL scripts in `doc/01.SQL/{install,patch}/` and must be applied to both local MySQL and production RDS.
- Single `application.yml`, no profiles. Production overrides DB/mail settings via env vars in `/etc/spring-playground.env`. S3 uses the EC2 IAM role, so file upload does not work locally.
- Secrets come from env vars, never literals in `application.yml`: `SPRING_DATASOURCE_PASSWORD` is required (startup fails without it); `SPRING_MAIL_PASSWORD` is optional (only mail sending fails). DB URL/username default to local values. Old literal values remain in git history.
- Docs live in `doc/` (README links to `docs/`, which is wrong).

## Deployment

Manual deploy to AWS EC2 (systemd service `spring-playground`, Nginx :80 → :9090, RDS MySQL, private S3). Follow `doc/deploy.md`; never start the app with a bare `java -jar` on the server (it misses the env file and connects to localhost DB). Known issues: `doc/troubleshooting.md`.

## Conventions

- Indentation is mixed (tabs/spaces); match the file being edited.
- Files are UTF-8 with CRLF line endings.
- Write code comments and commit messages in Korean (free-form, no Conventional Commits prefix).
