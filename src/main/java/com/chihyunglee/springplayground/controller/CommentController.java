package com.chihyunglee.springplayground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chihyunglee.springplayground.service.CommentService;

import com.chihyunglee.springplayground.security.CustomUserDetails;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addComment(@RequestParam Long postId,
                             @RequestParam(required = false) Long parentId,
                             @RequestParam String content,
                             @AuthenticationPrincipal CustomUserDetails currentUser) {
        commentService.addComment(postId, parentId, content, currentUser.getUser());
        return "redirect:/board/" + postId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                @AuthenticationPrincipal CustomUserDetails currentUser) {
        commentService.deleteComment(id, currentUser);
        return "redirect:/board/" + id;
    }
}
