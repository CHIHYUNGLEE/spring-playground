package com.chihyunglee.springplayground.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final S3FileService s3FileService;   // 첨푸파일 기능 추가(아마존 클라우드 S3)

    public List<BoardPost> findAll() {
        return boardRepository.findAll();
    }

    public Optional<BoardPost> findById(Long id) {
        return boardRepository.findById(id);
    }

    public BoardPost save(BoardPost post, MultipartFile file) throws IOException {
        String key = null;
        if (file != null && !file.isEmpty()) {
            key = s3FileService.upload(file);
            post.setFileKey(key);
            post.setFileName(file.getOriginalFilename());
        }
        try {
            return boardRepository.save(post);
        } catch (RuntimeException e) {
            if (key != null) s3FileService.delete(key);   // DB 저장 실패 시 S3 파일 정리
            throw e;
        }
    }

    public void delete(Long id, User currentUser) {
        BoardPost post = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        // Admin은 무조건 삭제 가능 / User는 본인 글만 삭제 가능
        if (!currentUser.getRole().contains("ROLE_ADMIN")
                && !post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("삭제 권한 없음");
        }

        boardRepository.delete(post);
        if (post.getFileKey() != null) {
            s3FileService.delete(post.getFileKey()); // 글 삭제 시 파일도 삭제
        }
    }

    public BoardPost update(Long id, BoardPost updatedPost, User currentUser) {
        BoardPost post = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        // Admin은 무조건 수정 가능 / User는 본인 글만 수정 가능
        if (!currentUser.getRole().contains("ROLE_ADMIN")
                && !post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("수정 권한 없음");
        }

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        return boardRepository.save(post);
    }    
}
