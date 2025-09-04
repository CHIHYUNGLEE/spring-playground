package com.chihyunglee.springplayground.service;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //암호화
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 로그인
    public boolean login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUserId(username);
        if (userOpt.isPresent()) {
            return passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
        }
        return false;
    }

    // 비밀번호 찾기 (가입 이메일 확인)
    public Optional<User> findByUsernameAndEmail(String userId, String email) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isPresent() && userOpt.get().getEmail().equals(email)) {
            return userOpt;
        }
        return Optional.empty();
    }
}
