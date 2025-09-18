package com.chihyunglee.springplayground.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                             @AuthenticationPrincipal CustomUserDetails currentUser,
                             RedirectAttributes redirectAttributes) {
    	  try {
    	        commentService.addComment(postId, parentId, content, currentUser);
    	  } catch (IllegalArgumentException e) {
    	        // 에러 메시지를 JSP에서 보여주기 위해 RedirectAttributes 사용
			  if(Optional.ofNullable(parentId).orElse(0L) > 0) {//대댓글
				  redirectAttributes.addFlashAttribute("errorReReplyMessage", e.getMessage());  
			  }else {
				  redirectAttributes.addFlashAttribute("errorReplyMessage", e.getMessage());
			  }
    	  }
    	  return "redirect:/board/" + postId;
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable Long id, @RequestParam Long postId,
    							@RequestParam(required = false) Long parentId,
    							@RequestParam String content,
    							@AuthenticationPrincipal CustomUserDetails currentUser,
    							RedirectAttributes redirectAttributes) {
    	  try {
    	        commentService.updateComment(id, parentId, content, currentUser);
    	  } catch (IllegalArgumentException e) {
    	        // 에러 메시지를 JSP에서 보여주기 위해 RedirectAttributes 사용
			  if(Optional.ofNullable(parentId).orElse(0L) > 0) {//대댓글
				  redirectAttributes.addFlashAttribute("errorReReplyMessage", e.getMessage());  
			  }else {
				  redirectAttributes.addFlashAttribute("errorReplyMessage", e.getMessage());
			  }
    	  }
    	  return "redirect:/board/" + postId;
    }

    

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam Long postId,
                                @AuthenticationPrincipal CustomUserDetails currentUser) 
    {
        commentService.deleteComment(id, currentUser);
        return "redirect:/board/" + postId;
    }
}
