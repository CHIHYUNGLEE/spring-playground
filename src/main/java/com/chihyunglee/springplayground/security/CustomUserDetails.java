package com.chihyunglee.springplayground.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private int status;

    public CustomUserDetails(String username, String password,
                             Collection<? extends GrantedAuthority> authorities,
                             int status) {
        super(username, password, authorities);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

