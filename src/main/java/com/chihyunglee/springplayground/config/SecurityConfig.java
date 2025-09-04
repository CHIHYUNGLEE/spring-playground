package com.chihyunglee.springplayground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 포트폴리오용 CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/findPassword", "/css/**", "/js/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/doLogin").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")              // 커스텀 login.jsp
                .loginProcessingUrl("/doLogin")   // POST 로그인 처리 URL
                .defaultSuccessUrl("/home", true) // 로그인 성공 후 홈
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            );
        return http.build();
    }
}
