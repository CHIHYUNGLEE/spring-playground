package com.chihyunglee.springplayground.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chihyunglee.springplayground.model.BoardPost;
import com.chihyunglee.springplayground.model.Comment;
import com.chihyunglee.springplayground.security.CustomUserDetails;
import com.chihyunglee.springplayground.service.BoardService;
import com.chihyunglee.springplayground.service.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    // 게시판 목록
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        model.addAttribute("posts", boardService.findAll());
        return "board/usr.bbs.list"; // list.jsp
    }

    // 글 읽기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String read(@PathVariable Long id, Model model) {
        BoardPost post = boardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        
        // 댓글 가져오기 (루트 댓글 10개)
        List<Comment> comments = commentService.getRootComments(id, 0, 10);
        // 각 댓글마다 하위 댓글도 fetch or set
        comments.forEach(c -> c.setReplies(commentService.getReplies(c.getId())));

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "board/usr.bbs.read"; // read.jsp
    }

    // 새 글 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("post", new BoardPost());
        return "board/usr.bbs.write";
    }

    // 새 글 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/save")
    public String create(@ModelAttribute BoardPost post,
                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        post.setAuthor(currentUser.getUser());
        boardService.save(post);
        return "redirect:/board/list";
    }

    // 수정 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        BoardPost post = boardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        model.addAttribute("post", post);
        return "board/usr.bbs.edit"; // edit.jsp
    }

    // 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute BoardPost updatedPost,
                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        boardService.update(id, updatedPost, currentUser.getUser());
        return "redirect:/board/list";
    }

    // 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        boardService.delete(id, currentUser.getUser());
        return "redirect:/board/list";
    }
}