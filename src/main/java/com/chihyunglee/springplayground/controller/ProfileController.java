package com.chihyunglee.springplayground.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.service.UserService;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // 회원정보 수정 폼 열기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfileForm(Model model, Principal principal) {
    	
        // 로그인한 사용자 아이디 가져오기
        String userId = principal.getName();
        
        // DB에서 사용자 정보 가져오기
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);

        return "profileForm"; // JSP 뷰
    }

    // 회원정보 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User userForm, Principal principal, RedirectAttributes redirectAttributes) {
    	String userId = principal.getName();

        userService.updateUser(userId, userForm);

        redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었어요 💕");
        return "redirect:/profile";
    }
}

