package com.chihyunglee.springplayground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.chihyunglee.springplayground.service.CustomUserDetailsService;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        	// FORWARD 타입의 디스패처 요청에 대해서는 모두 허용합니다.
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() 
            .requestMatchers("/", "/home", "/login", "/register", "/findPassword",
                            "/css/**", "/js/**", "/images/**", "/doLogin", "/error").permitAll()
            .anyRequest().authenticated() // 아직은 모든 요청 허용
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/doLogin")
            .usernameParameter("userId")
            .passwordParameter("password")
            .defaultSuccessUrl("/home", true)
            .failureUrl("/login?error=true")
            .permitAll()
        );

        return http.build();
    }
}
