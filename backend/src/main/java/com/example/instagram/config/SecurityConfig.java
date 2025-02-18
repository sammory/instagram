package com.example.instagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // 특정 요청에 대해 CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register").permitAll() // 회원가입 API는 인증 없이 허용
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )
                .formLogin(form -> form.disable()); // 기본 로그인 폼 비활성화 (API 방식 사용)

        return http.build();
    }
}
