package com.adelok.marketplace_auth.config;

import com.adelok.marketplace_auth.security.CustomUserDetailsService;
import com.adelok.marketplace_auth.security.JwtAuthFilter;
import com.adelok.marketplace_auth.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter authFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login" , "/api/v1/auth/refresh").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthFilter authFilter (CustomUserDetailsService customUserDetailsService, JwtService jwtService){
return new JwtAuthFilter(customUserDetailsService, jwtService);
    }

}
