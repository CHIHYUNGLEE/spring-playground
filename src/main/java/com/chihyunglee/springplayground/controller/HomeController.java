package com.chihyunglee.springplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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
    public String login() {
    	System.out.println("Login controller called");
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
