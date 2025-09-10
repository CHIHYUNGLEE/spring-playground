package com.chihyunglee.springplayground.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.Comment;
import com.chihyunglee.springplayground.model.User;
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
    public void addComment(Long postId, Long parentId, String content, User user) {
    	
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
        comment.setUser(user);
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, CustomUserDetails currentUser) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (currentUser.getUser().getRole().equals("ROLE_ADMIN") || comment.getUser().getId().equals(currentUser.getUser().getId())) {
            commentRepository.delete(comment);
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


