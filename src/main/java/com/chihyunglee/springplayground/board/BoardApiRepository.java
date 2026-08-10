package com.chihyunglee.springplayground.board;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardApiRepository extends JpaRepository<Board, Long> {
	Page<Board> findByTitleContaining( 
			String keyword, 
			Pageable pageable 
	);
}
