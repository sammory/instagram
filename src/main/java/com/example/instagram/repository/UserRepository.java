package com.example.instagram.repository;

import com.example.instagram.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자명(username)으로 사용자 조회 (로그인 시 사용)
    Optional<User> findByUsername(String username);

    // 이메일(email)로 사용자 조회 (이메일 중복 확인)
    Optional<User> findByEmail(String email);
}
