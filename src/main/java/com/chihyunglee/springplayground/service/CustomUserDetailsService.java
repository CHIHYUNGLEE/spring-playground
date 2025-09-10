package com.chihyunglee.springplayground.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.UserRepository;
import com.chihyunglee.springplayground.security.CustomUserDetails;
import com.chihyunglee.springplayground.security.exception.UserWithdrawnException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));
        
        if(user.getStatus() == 9100) {
            throw new UserWithdrawnException("탈퇴한 사용자입니다.");
        }

        // Spring Security UserDetails 객체 생성 
        return new CustomUserDetails(user);
    }
}
