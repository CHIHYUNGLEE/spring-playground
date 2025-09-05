package com.chihyunglee.springplayground.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chihyunglee.springplayground.model.BoardPost;

public interface BoardRepository extends JpaRepository<BoardPost, Long> {
}
