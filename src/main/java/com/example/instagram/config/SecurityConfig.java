package com.example.instagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 이 클래스가 Spring의 설정 클래스임을 명시
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder를 Spring Bean으로 등록
     * - 비밀번호를 안전하게 암호화하는 데 사용
     * - @Autowired 또는 @RequiredArgsConstructor를 통해 주입 가능
     * - 모든 서비스에서 동일한 인스턴스를 재사용할 수 있음
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
