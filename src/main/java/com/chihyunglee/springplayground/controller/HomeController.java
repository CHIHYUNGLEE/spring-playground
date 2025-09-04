package com.chihyunglee.springplayground.controller;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    // 홈
    @GetMapping("/home")
    public String home() { return "home"; }

    // 로그인 화면
    @GetMapping("/login")
    public String login() { return "login"; }

    // 로그인 처리
    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model) {
        boolean success = userService.login(username, password);
        if (success) {
            model.addAttribute("username", username);
            return "home";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "login";
        }
    }

    // 회원가입 화면
    @GetMapping("/register")
    public String register() { return "register"; }

    // 회원가입 처리
    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             Model model) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        userService.register(user);
        model.addAttribute("msg", "회원가입 성공! 로그인 해주세요.");
        return "login";
    }

    // 비밀번호 찾기 화면
    @GetMapping("/findPassword")
    public String findPassword() { return "findPassword"; }

    // 비밀번호 찾기 처리
    @PostMapping("/findPassword")
    public String doFindPassword(@RequestParam String username,
                                 @RequestParam String email,
                                 Model model) {
        var userOpt = userService.findByUsernameAndEmail(username, email);
        if (userOpt.isPresent()) {
            // 포트폴리오용이라 임시 비밀번호 출력 (실서비스면 이메일 발송)
            model.addAttribute("msg", "임시 비밀번호: temp1234");
        } else {
            model.addAttribute("msg", "아이디 또는 이메일이 잘못되었습니다.");
        }
        return "findPassword";
    }
}
