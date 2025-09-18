package com.chihyunglee.springplayground.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.Comment;
import com.chihyunglee.springplayground.repository.BoardRepository;
import com.chihyunglee.springplayground.repository.CommentRepository;
import com.chihyunglee.springplayground.security.CustomUserDetails;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository postRepository;

    public List<Comment> getRootComments(Long postId, int page, int size) {
        return commentRepository.findRootCommentsByPostId(postId, PageRequest.of(page, size));
    }

    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findByParentIdOrderByCreatedAtAsc(parentId);
    }

    @Transactional
    public Comment addComment(Long postId, Long parentId, String content, CustomUserDetails currentUser) {
    	
        if (content == null || content.trim().isEmpty()) {
        	if(Optional.ofNullable(parentId).orElse(0L) > 0) {
        		throw new IllegalArgumentException("대댓글 내용을 입력해야 합니다.");
        	}else {
        		throw new IllegalArgumentException("댓글 내용을 입력해야 합니다.");
        	}
        }
    	
        Comment comment = new Comment();
        BoardPost post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId).orElseThrow();
            
            // 🔹 대댓글 단계 제한
            if (getDepth(parent) >= 3) { // 3단계 이상이면 예외
                throw new IllegalArgumentException("대댓글 작성은 최대 3단계까지 가능합니다.");
            }
            
            comment.setParent(parent);
        }
        comment.setUser(currentUser.getUser());
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, Long parentId, String content, CustomUserDetails currentUser) {
    	
        if (content == null || content.trim().isEmpty()) {
        	if(Optional.ofNullable(parentId).orElse(0L) > 0) {
        		throw new IllegalArgumentException("대댓글 내용을 입력해야 합니다.");
        	}else {
        		throw new IllegalArgumentException("댓글 내용을 입력해야 합니다.");
        	}
        }
    	
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (currentUser.getUser().getRole().equals("ADMIN") || comment.getUser().getId().equals(currentUser.getUser().getId())) {
	        comment.setContent(content);
        } else {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }
    
    @Transactional
    public void deleteComment(Long commentId, CustomUserDetails currentUser) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (currentUser.getUser().getRole().equals("ADMIN") || comment.getUser().getId().equals(currentUser.getUser().getId())) {
            // 자식 댓글이 있는지 확인
            boolean hasChildren = !comment.getReplies().isEmpty();

            if (hasChildren) {
                // 실제 삭제 대신 "삭제된 댓글입니다."로 표시
                comment.setContent("삭제된 댓글입니다.");
                // 원한다면 작성자도 null 처리 가능 (익명화)
                // comment.setUser(null);
            } else {
                // 자식 없으면 실제 삭제
                commentRepository.delete(comment);
            }
        } else {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }
    
    // 깊이 계산 메서드
    private int getDepth(Comment comment) {
        int depth = 1;
        Comment parent = comment.getParent();
        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }
        return depth;
    }
}


