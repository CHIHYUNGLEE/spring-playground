package com.chihyunglee.springplayground.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

	//에러페이지로 이동
    @RequestMapping("/error")
    public String handleError() {
        return "error"; // /WEB-INF/views/error.jsp
    }
}