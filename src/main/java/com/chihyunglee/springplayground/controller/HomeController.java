package com.chihyunglee.springplayground.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "home"; // Security가 인증 관리
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // 세션 체크 제거, Security가 처리
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "findPassword";
    }
}
