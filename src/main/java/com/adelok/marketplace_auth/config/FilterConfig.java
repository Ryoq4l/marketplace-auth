package com.adelok.marketplace_auth.config;

import com.adelok.marketplace_auth.security.CustomUserDetailsService;
import com.adelok.marketplace_auth.security.JwtAuthFilter;
import com.adelok.marketplace_auth.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter(CustomUserDetailsService customUserDetailsService, JwtService jwtService) {
        return new JwtAuthFilter(customUserDetailsService, jwtService);
    }
}