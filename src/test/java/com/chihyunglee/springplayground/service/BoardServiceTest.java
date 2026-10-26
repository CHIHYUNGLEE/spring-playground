package com.chihyunglee.springplayground.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.BoardRepository;

/**
 * BoardService 단위 테스트
 * - Spring 컨텍스트, DB, S3 없이 Mockito mock 으로만 실행
 */
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private S3FileService s3FileService;

    @InjectMocks
    private BoardService boardService;

    // 테스트용 사용자
    private static User user(long id, String role) {
        User u = new User();
        u.setId(id);
        u.setUserId("user" + id);
        u.setRole(role);
        return u;
    }

    // 테스트용 게시글
    private static BoardPost post(long id, User author) {
        BoardPost p = new BoardPost();
        p.setId(id);
        p.setTitle("원래 제목");
        p.setContent("원래 내용");
        p.setAuthor(author);
        return p;
    }

    @Nested
    @DisplayName("제목 검색 searchByTitle")
    class SearchByTitle {

        @Test
        @DisplayName("검색어가 null 이면 전체 목록")
        void nullKeyword() {
            List<BoardPost> all = List.of(new BoardPost());
            when(boardRepository.findAll()).thenReturn(all);

            assertThat(boardService.searchByTitle(null)).isSameAs(all);
            verify(boardRepository, never()).findByTitleContaining(anyString());
        }

        @Test
        @DisplayName("검색어가 공백뿐이면 전체 목록")
        void blankKeyword() {
            when(boardRepository.findAll()).thenReturn(List.of());

            boardService.searchByTitle("   ");

            verify(boardRepository).findAll();
            verify(boardRepository, never()).findByTitleContaining(anyString());
        }

        @Test
        @DisplayName("검색어 앞뒤 공백을 제거해서 제목 검색")
        void trimsKeyword() {
            List<BoardPost> found = List.of(new BoardPost());
            when(boardRepository.findByTitleContaining("스프링")).thenReturn(found);

            assertThat(boardService.searchByTitle("  스프링 ")).isSameAs(found);
            verify(boardRepository, never()).findAll();
        }
    }

    @Nested
    @DisplayName("글 저장 save")
    class Save {

        @Test
        @DisplayName("첨부파일이 없으면 S3 업로드 없이 저장")
        void withoutFile() throws Exception {
            BoardPost p = new BoardPost();
            when(boardRepository.save(p)).thenReturn(p);

            boardService.save(p, null);

            verify(s3FileService, never()).upload(any());
            assertThat(p.getFileKey()).isNull();
        }

        @Test
        @DisplayName("빈 파일이면 S3 업로드하지 않음")
        void emptyFile() throws Exception {
            BoardPost p = new BoardPost();
            MockMultipartFile empty = new MockMultipartFile("file", "a.txt", "text/plain", new byte[0]);
            when(boardRepository.save(p)).thenReturn(p);

            boardService.save(p, empty);

            verify(s3FileService, never()).upload(any());
        }

        @Test
        @DisplayName("첨부파일이 있으면 S3 업로드 후 key와 원래 파일명을 저장")
        void withFile() throws Exception {
            BoardPost p = new BoardPost();
            MockMultipartFile file = new MockMultipartFile("file", "보고서.pdf", "application/pdf", "data".getBytes());
            when(s3FileService.upload(file)).thenReturn("board/uuid.pdf");
            when(boardRepository.save(p)).thenReturn(p);

            boardService.save(p, file);

            assertThat(p.getFileKey()).isEqualTo("board/uuid.pdf");
            assertThat(p.getFileName()).isEqualTo("보고서.pdf");
        }

        @Test
        @DisplayName("DB 저장이 실패하면 업로드한 S3 파일을 지우고 예외를 다시 던짐")
        void dbFailureCleansUpS3() throws Exception {
            BoardPost p = new BoardPost();
            MockMultipartFile file = new MockMultipartFile("file", "a.png", "image/png", "img".getBytes());
            when(s3FileService.upload(file)).thenReturn("board/uuid.png");
            when(boardRepository.save(p)).thenThrow(new RuntimeException("DB 오류"));

            assertThatThrownBy(() -> boardService.save(p, file))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("DB 오류");
            verify(s3FileService).delete("board/uuid.png");
        }

        @Test
        @DisplayName("첨부파일 없이 DB 저장이 실패하면 S3 삭제를 호출하지 않음")
        void dbFailureWithoutFile() {
            BoardPost p = new BoardPost();
            when(boardRepository.save(p)).thenThrow(new RuntimeException("DB 오류"));

            assertThatThrownBy(() -> boardService.save(p, null)).isInstanceOf(RuntimeException.class);
            verify(s3FileService, never()).delete(anyString());
        }
    }

    @Nested
    @DisplayName("글 삭제 delete")
    class Delete {

        @Test
        @DisplayName("작성자 본인은 삭제 가능, 첨부파일도 DB 삭제 후 S3에서 삭제")
        void ownerDeletesWithFile() {
            User owner = user(1L, User.ROLE_USER);
            BoardPost p = post(10L, owner);
            p.setFileKey("board/uuid.png");
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));

            boardService.delete(10L, owner);

            InOrder order = inOrder(boardRepository, s3FileService);
            order.verify(boardRepository).delete(p);
            order.verify(s3FileService).delete("board/uuid.png");
        }

        @Test
        @DisplayName("첨부파일이 없으면 S3 삭제를 호출하지 않음")
        void ownerDeletesWithoutFile() {
            User owner = user(1L, User.ROLE_USER);
            BoardPost p = post(10L, owner);
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));

            boardService.delete(10L, owner);

            verify(boardRepository).delete(p);
            verify(s3FileService, never()).delete(anyString());
        }

        @Test
        @DisplayName("다른 일반 사용자는 삭제 불가")
        void otherUserDenied() {
            BoardPost p = post(10L, user(1L, User.ROLE_USER));
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));

            assertThatThrownBy(() -> boardService.delete(10L, user(2L, User.ROLE_USER)))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("삭제 권한 없음");
            verify(boardRepository, never()).delete(any());
        }

        @Test
        @DisplayName("관리자는 남의 글도 삭제 가능")
        void adminDeletesOthersPost() {
            BoardPost p = post(10L, user(1L, User.ROLE_USER));
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));

            boardService.delete(10L, user(99L, User.ROLE_ADMIN));

            verify(boardRepository).delete(p);
        }

        @Test
        @DisplayName("없는 글이면 IllegalArgumentException")
        void notFound() {
            when(boardRepository.findById(10L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> boardService.delete(10L, user(1L, User.ROLE_USER)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("게시글 없음");
        }
    }

    @Nested
    @DisplayName("글 수정 update")
    class Update {

        @Test
        @DisplayName("작성자 본인은 제목과 내용만 수정")
        void ownerUpdates() {
            User owner = user(1L, User.ROLE_USER);
            BoardPost p = post(10L, owner);
            p.setFileKey("board/keep.png");
            BoardPost changes = new BoardPost();
            changes.setTitle("새 제목");
            changes.setContent("새 내용");
            changes.setFileKey("board/other.png");
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));
            when(boardRepository.save(p)).thenReturn(p);

            BoardPost result = boardService.update(10L, changes, owner);

            assertThat(result.getTitle()).isEqualTo("새 제목");
            assertThat(result.getContent()).isEqualTo("새 내용");
            assertThat(result.getFileKey()).isEqualTo("board/keep.png"); // 첨부파일은 바뀌지 않음
            assertThat(result.getAuthor()).isSameAs(owner);
        }

        @Test
        @DisplayName("다른 일반 사용자는 수정 불가, 원본도 그대로")
        void otherUserDenied() {
            BoardPost p = post(10L, user(1L, User.ROLE_USER));
            BoardPost changes = new BoardPost();
            changes.setTitle("해킹");
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));

            assertThatThrownBy(() -> boardService.update(10L, changes, user(2L, User.ROLE_USER)))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("수정 권한 없음");
            assertThat(p.getTitle()).isEqualTo("원래 제목");
            verify(boardRepository, never()).save(any());
        }

        @Test
        @DisplayName("관리자는 남의 글도 수정 가능")
        void adminUpdatesOthersPost() {
            BoardPost p = post(10L, user(1L, User.ROLE_USER));
            BoardPost changes = new BoardPost();
            changes.setTitle("관리자 수정");
            changes.setContent("관리자 내용");
            when(boardRepository.findById(10L)).thenReturn(Optional.of(p));
            when(boardRepository.save(p)).thenReturn(p);

            assertThat(boardService.update(10L, changes, user(99L, User.ROLE_ADMIN)).getTitle())
                    .isEqualTo("관리자 수정");
        }

        @Test
        @DisplayName("없는 글이면 IllegalArgumentException")
        void notFound() {
            when(boardRepository.findById(10L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> boardService.update(10L, new BoardPost(), user(1L, User.ROLE_USER)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("게시글 없음");
        }
    }
}
