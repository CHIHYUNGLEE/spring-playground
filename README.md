# spring-playground
스프링프레임워크용 playground

## Vue.js + Spring Boot CRUD 연동 (2026.08)

SPA 프레임워크 학습을 위해 진행한 실습입니다.
기존 서버사이드 게시판은 그대로 두고, REST API 계층을 별도로 추가해 Vue 화면에서 호출하는 구조로 구현했습니다.

### 구조
- 백엔드: Controller → BoardApiService → BoardApiRepository → DB
- BoardDTO를 도입해 엔티티 직접 노출 제거
- 기존 게시판(서버사이드 렌더링)과 API 계층을 클래스 단위로 분리
- 프론트가 별도 포트에서 동작하므로 `/api/**` 인증 예외 및 CORS 설정을 분리

### 기능
- 목록 조회 / 등록 / 수정 / 삭제 / 상세 조회
- 제목 검색, 페이지 단위 목록 조회
- 최신순 / 오래된순 정렬
- 페이지당 10 / 20 / 50 / 100건 선택
- 페이지 번호가 많아지면 생략 표시(...) 처리
- 페이지 이동 시 검색 조건 유지
- 진행 예정 : 예외 처리 / 공통 응답 DTO, createdAt/updatedAt 추가, Swagger API 문서화, 테스트코드 추가(JUnit + Spring Boot Test + MockMvc)

### 실행 방법

백엔드

```
./mvnw spring-boot:run
```


프론트

```
cd vue-board
npm install
npm run dev
```


### url

| 구분 | 주소 |
|---|---|
| Vue 게시판 화면 | http://localhost:5173/board |
| REST API | http://localhost:9090/api/posts |
| 기존 서버사이드 게시판 | http://localhost:9090/ |


### 화면
**Vue 게시판 목록/상세조회/수정**

<img width="941" height="660" alt="image" src="https://github.com/user-attachments/assets/6bf1dd67-1b54-412c-9e35-1be3f9767f1e" />

<img width="1099" height="617" alt="image" src="https://github.com/user-attachments/assets/1df2e8ee-49e3-4cb3-aaaf-3ea3ad115c12" />

<img width="1270" height="847" alt="image" src="https://github.com/user-attachments/assets/9e2aecfa-91ff-4884-a719-60a9749bcad1" />


**로그인 화면**

<img width="789" height="408" alt="image" src="https://github.com/user-attachments/assets/cc9eb5b3-0395-4185-9d00-1560695593f3" />

