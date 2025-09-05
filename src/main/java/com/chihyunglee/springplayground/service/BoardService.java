package com.chihyunglee.springplayground.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardPost> findAll() {
        return boardRepository.findAll();
    }

    public Optional<BoardPost> findById(Long id) {
        return boardRepository.findById(id);
    }

    public BoardPost save(BoardPost post) {
        return boardRepository.save(post);
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
