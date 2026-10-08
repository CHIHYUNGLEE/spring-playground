# 화면(프론트엔드) 구조

> 작성 기준: 2026-10-08  
> 이 프로젝트에는 게시판 화면이 **3벌** 있습니다. 헷갈리면 이 표부터 봅니다.

## 한눈에 보기

| 화면 | 백엔드 진입점 | 엔티티 | 테이블 | 배포 | 첨부파일 |
|---|---|---|---|---|---|
| **JSP** | `controller.BoardController` (`/board/*`) | `model.BoardPost` | `board_posts` | ✅ EC2 (war에 포함) | ✅ |
| React | `/api/posts` | `board.Board` | `board` | ❌ 로컬만 | ❌ |
| Vue | `/api/posts` | `board.Board` | `board` | ❌ 로컬만 | ❌ |

- React와 Vue는 **같은 REST API**를 쓰는 프론트 2개
- JSP는 별도 게시판. 로그인·권한·댓글이 붙어 있음
- 서버에 배포되어 `http://<EC2_PUBLIC_IP>`로 보이는 것은 **JSP뿐**

## JSP 게시판

| 구분 | 위치 |
|---|---|
| 컨트롤러 | `controller/BoardController.java` |
| 서비스 | `service/BoardService.java` |
| 엔티티 | `model/BoardPost.java` (`@Table(name = "board_posts")`) |
| 뷰 | `src/main/webapp/WEB-INF/views/board/usr.bbs.*.jsp` |

주요 URL: `/board/list` `/board/{id}` `/board/new` `/board/save` `/board/edit/{id}` `/board/delete/{id}`

### 첨부파일 (2026-10 추가)
- 글당 파일 1개 / 최대 10MB
- 저장: `S3FileService.upload()` → S3 `board/<UUID>.<확장자>`
- DB: `board_posts.file_key` `board_posts.file_name`
- 다운로드: 상세 화면에서 5분 유효 Presigned URL 링크
- 삭제: 글 삭제 시 S3 파일도 삭제
- 글쓰기 폼: `enctype="multipart/form-data"` + `<input type="file" name="file">`
- 로컬 실행 시 IAM Role이 없어서 업로드는 실패함 (테스트는 EC2에서)

## React / Vue 게시판

| 구분 | 내용 |
|---|---|
| API | `/api/posts` |
| 엔티티 | `board/Board.java` (`@Table(name="board")`) |
| Repository | `BoardApiRepository` |
| Vue 상태관리 | `stores/board.js` (`addPost`) |

- React 글쓰기에 API 주소 `http://localhost:9090` 하드코딩 → 배포 시 수정 필요
- `board` 테이블에도 `file_key` `file_name` 컬럼은 추가되어 있으나 미사용

## 앞으로
- React·Vue는 나중에 배포 예정
- 배포 시 `npm run build` 결과물을 Nginx가 정적 파일로 서빙하고 `/api`만 Spring으로 프록시하는 구조 검토
