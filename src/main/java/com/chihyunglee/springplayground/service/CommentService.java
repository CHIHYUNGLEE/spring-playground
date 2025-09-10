package com.chihyunglee.springplayground.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.Comment;
import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.BoardRepository;
import com.chihyunglee.springplayground.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.List;

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
        Comment comment = new Comment();
        BoardPost post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId).orElseThrow();
            comment.setParent(parent);
        }
        comment.setUser(user);
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (currentUser.getRole().equals("ROLE_ADMIN") || comment.getUser().getId().equals(currentUser.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }
}


