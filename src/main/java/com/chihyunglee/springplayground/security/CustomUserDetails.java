package com.chihyunglee.springplayground.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.chihyunglee.springplayground.model.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private int status;
    private User user; // 실제 User 엔티티 추가

    public CustomUserDetails(User user) {
        super(user.getUserId(),
              user.getPassword(),
              Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));// 권한 처리 (ADMIN / USER)
        this.user = user;
        this.status = user.getStatus();
    }

    public int getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}

