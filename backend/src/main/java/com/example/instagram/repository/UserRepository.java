package com.example.instagram.repository;

import com.example.instagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 아이디(username) 중복 확인
    boolean existsByUsername(String username);

    // 로그인 시 사용
    Optional<User> findByEmail(String email);
}
