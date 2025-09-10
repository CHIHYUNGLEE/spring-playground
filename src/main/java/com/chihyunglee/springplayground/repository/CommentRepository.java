package com.chihyunglee.springplayground.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chihyunglee.springplayground.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글의 루트 댓글 (parent IS NULL) 페이징
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findRootCommentsByPostId(@Param("postId") Long postId, Pageable pageable);

    // 댓글의 하위 댓글
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
}
