package com.chihyunglee.springplayground.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.service.UserService;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')") // 관리자만 접근 가능
public class UserController {
	
    private final UserService userService;

    // 생성자 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }
	
    // 회원 목록 (status 필터: ALL / ACTIVE / INACTIVE)
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "ALL") String status, Model model) {
        List<User> users;
        if ("ACTIVE".equalsIgnoreCase(status)) {
            users = userService.findByStatus(User.STATUS_NORMAL); // 정상
        } else if ("INACTIVE".equalsIgnoreCase(status)) {
            users = userService.findByStatus(User.STATUS_WITHDRAW); // 탈퇴
        } else {
            users = userService.findAll();
        }

        model.addAttribute("users", users);
        model.addAttribute("status", status);
        return "user/userList"; // JSP 뷰
    }

    // 일반권한 부여 (ROLE 업데이트)
    @PostMapping("/grantUserRole/{id}")
    public String grantUserRole(@PathVariable Long id) {
        userService.grantUserByAdmin(id); // role을 admin으로 업데이트
        return "redirect:/user/list?status=ALL";
    }
    
    // 관리자 부여 (ROLE 업데이트)
    @PostMapping("/grantAdminRole/{id}")
    public String grantAdminRole(@PathVariable Long id) {
        userService.grantAdminByAdmin(id); // role을 admin으로 업데이트
        return "redirect:/user/list?status=ALL";
    }

    // 회원 복귀 (status 업데이트)
    @PostMapping("/activate/{id}")
    public String activate(@PathVariable Long id) {
        userService.registerUserByAdmin(id); // status = 0 처리
        return "redirect:/user/list?status=ALL";
    }
    
    // 회원 탈퇴 (status 업데이트)
    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Long id) {
        userService.withdrawUserByAdmin(id); // status = 0 처리
        return "redirect:/user/list?status=ALL";
    }

    // 회원 물리적 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUserPermanently(id); // 실제 DB 삭제
        return "redirect:/user/list?status=ALL";
    }
}
