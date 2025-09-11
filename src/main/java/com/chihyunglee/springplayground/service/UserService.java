package com.chihyunglee.springplayground.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.model.User;
import com.chihyunglee.springplayground.repository.UserRepository;

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

    // 비밀번호 찾기 (가입 이메일 확인)
    public Optional<User> findByUsernameAndEmail(String userId, String email) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isPresent() && userOpt.get().getEmail().equals(email)) {
            return userOpt;
        }
        return Optional.empty();
    }
    
    //모든회원 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    // 사용자 정보 조회(아이디)
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " 사용자를 찾을 수 없습니다."));
    }

    // 사용자 정보 조회(이름)
//    public User findByUsername(String username) {
//        return userRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username + " 사용자를 찾을 수 없습니다."));
//    }

    // 사용자 정보 업데이트
    public void updateUser(String userId, User userForm) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " 사용자를 찾을 수 없습니다."));

        user.setUserName(userForm.getUserName());
        user.setEmail(userForm.getEmail());

        userRepository.save(user);
    }
    
    // 상태값으로 찾는 회원목록
    public List<User> findByStatus(int status) {
        return userRepository.findByStatus(status);
    }
    
    // 회원 탈퇴
    public void withdrawUser(String userId) {
        User user = userRepository.findByUserId(userId)
                                  .orElseThrow(() -> new UsernameNotFoundException(userId + " 사용자를 찾을 수 없습니다."));
        user.setStatus(9100); // 탈퇴 상태로만 업데이트
        userRepository.save(user);
    }
    
    // 일반 권한 부여(관리자용)
    public void grantUserByAdmin(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.setRole(User.ROLE_USER); // 관리자로 role 변경
        userRepository.save(user);
    }
    
    // 관리자 권한 부여(관리자용)
    public void grantAdminByAdmin(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.setRole(User.ROLE_ADMIN); // 관리자로 role 변경
        userRepository.save(user);
    }
    
    // 회원 복귀(관리자용)
    public void registerUserByAdmin(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.setStatus(User.STATUS_NORMAL); // 탈퇴 상태로만 업데이트
        userRepository.save(user);
    }
    
    // 회원 탈퇴(관리자용)
    public void withdrawUserByAdmin(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.setStatus(User.STATUS_WITHDRAW); // 탈퇴 상태로만 업데이트
        userRepository.save(user);
    }
    
    // 회원 폐기(관리자만 가능)
    public void deleteUserPermanently(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getStatus() == User.STATUS_WITHDRAW) { // 탈퇴 상태인 경우만 물리 삭제 허용
            userRepository.delete(user);
        } else {
            throw new IllegalStateException("정상 회원은 삭제할 수 없습니다.");
        }
    }
}
