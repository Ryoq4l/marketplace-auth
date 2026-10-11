package com.adelok.marketplace_auth.security;

import com.adelok.marketplace_auth.config.JwtProperties;
import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.entity.enumerated.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class JwtServiceTest {
    private JwtService jwtService;
    JwtProperties properties = new JwtProperties();
    @BeforeEach
    void setUp() {
properties.setSecret("a9D2fG7hJ4kL1mN6pQ3rS8tU5vW0xY2z");
properties.setAccessTokenExpiration(900000);
properties.setRefreshTokenExpiration(604800000);
jwtService = new JwtService(properties);
    }
    @Test
    @DisplayName("generate valid access token")
    public void generateAccessToken(){
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("burmalda@gmail.com")
                .role(Role.USER)
                .build();
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}