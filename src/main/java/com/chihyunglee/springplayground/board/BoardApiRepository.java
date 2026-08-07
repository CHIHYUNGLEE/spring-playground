package com.chihyunglee.springplayground.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardApiRepository extends JpaRepository<Board, Long> {

}
