package com.chihyunglee.springplayground.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.UserRepository;
import com.chihyunglee.springplayground.service.MailService;

@Controller
public class PasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/findPassword")
    public String sendResetLink(@RequestParam String username,
                                @RequestParam String email,
                                Model model) {

        Optional<User> userOpt = userRepository.findByUserNameAndEmail(username, email);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "아이디와 이메일이 일치하지 않습니다.");
            return "findPassword";
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        user.setResetToken(token);
        user.setResetTokenExpiry(expiry);
        userRepository.save(user);

        String resetLink = "http://localhost:8080/resetPassword?token=" + token;

        mailService.sendMail(email, "비밀번호 재설정 안내",
                "아래 링크를 눌러 비밀번호를 재설정하세요:\n" + resetLink);

        model.addAttribute("message", "비밀번호 재설정 링크를 이메일로 보냈습니다 💌");
        return "findPassword";
    }

    @GetMapping("/resetPassword")
    public String showResetForm(@RequestParam String token, Model model) {
        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isEmpty() || userOpt.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "토큰이 만료되었거나 유효하지 않습니다.");
            return "resetPasswordError";
        }

        model.addAttribute("token", token);
        return "resetPasswordForm";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {

        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isEmpty() || userOpt.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "토큰이 만료되었거나 유효하지 않습니다.");
            return "resetPasswordError";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("token", token);
            return "resetPasswordForm";
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다 ✨");
        return "login";
    }

}

