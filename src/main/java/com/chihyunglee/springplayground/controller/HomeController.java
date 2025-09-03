package com.chihyunglee.springplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // /WEB-INF/views/home.jsp 로 연결
    }
}