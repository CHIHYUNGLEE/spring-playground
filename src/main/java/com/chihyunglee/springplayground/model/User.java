package com.chihyunglee.springplayground.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "users")
public class User {

	public static final int STATUS_NORMAL = 0;    // 정상 상태
    public static final int STATUS_WITHDRAW = 9100;  // 탈퇴 상태
    
	public static final String ROLE_ADMIN = "ADMIN";    // 관리자 상태
    public static final String ROLE_USER = "USER";  // 일반 상태
    
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String role = ROLE_USER;

    //회원가입일자 및 정보 수정일자
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    //비번 변경용 토큰 및 토큰 만료
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    
    //상태값
    private int status;
}
