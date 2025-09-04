package com.chihyunglee.springplayground.repository;

import com.chihyunglee.springplayground.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userId);
    Optional<User> findByEmail(String email);
}
