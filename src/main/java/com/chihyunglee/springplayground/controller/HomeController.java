package com.chihyunglee.springplayground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService; // ✅ 인스턴스 주입

    @GetMapping("/")
    public String root() {
    	System.out.println("Root controller called");
        return "home"; // 별도의 랜딩 페이지
    }

    @GetMapping("/home")
    public String home() {
    	System.out.println("home controller called");
        return "home"; // 로그인 후 접근하는 홈
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            org.springframework.ui.Model model) {
		System.out.println("Login controller called");

		if (error != null) {
			model.addAttribute("errorMsg", "아이디 또는 비밀번호가 틀렸습니다.");
		}

		return "login"; // login.jsp로 이동
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String userId,
    						 @RequestParam String userName,
                             @RequestParam String password,
                             @RequestParam String email,
                             Model model) {

        // 이메일 유효성 체크
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            model.addAttribute("emailError", "유효하지 않은 이메일 형식입니다.");
            return "register"; // register.jsp로 돌아감
        }

        // User 객체 생성
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);

        // DB 저장
        userService.register(user);

        // 성공 메시지
        model.addAttribute("registerSuccessMsg", "회원가입 성공! 로그인 해주세요.");

        return "login"; // 가입 후 로그인 페이지로 이동
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "findPassword";
    }
}
