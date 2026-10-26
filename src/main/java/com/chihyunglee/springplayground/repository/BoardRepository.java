package com.chihyunglee.springplayground.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chihyunglee.springplayground.model.BoardPost;

public interface BoardRepository extends JpaRepository<BoardPost, Long> {

    // 제목에 keyword가 포함된 게시글 조회 (LIKE %keyword%)
    List<BoardPost> findByTitleContaining(String keyword);
}
