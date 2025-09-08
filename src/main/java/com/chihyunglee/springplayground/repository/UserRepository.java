package com.chihyunglee.springplayground.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chihyunglee.springplayground.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserNameAndEmail(String username, String email);
    Optional<User> findByResetToken(String token);
}
