package com.chihyunglee.springplayground.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
	
	
	/** 실행 과정
	 * 1. 이클립스에서 Tomcat 서버 추가 (Servers 탭 → New → Tomcat v9.0 Server).
	 * 2. 프로젝트를 Tomcat 서버에 Add → Run.
	 * 3. 브라우저에서 http://localhost:8080/spring-playground 접속.
	 *	->화면에 Hello, Spring Playground! 나오면 성공!
	 */
	
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Hello, Spring Playground!";
    }
}
