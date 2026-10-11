package com.adelok.marketplace_auth.security;

import com.adelok.marketplace_auth.config.JwtProperties;
import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.entity.enumerated.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
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
jwtService.initSigningKey();
    }
    @Test
    @DisplayName("generate valid access token")
    public void generateAccessToken(){
        String steamId = "76561198000000000";
        User user = User.builder()
                .id(UUID.randomUUID())
                .steamId(steamId)
                .role(Role.USER)
                .build();
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);
        Claims claims = jwtService.extractClaims(token);
        assertNotNull(claims);
        assertEquals(steamId, claims.getSubject());
        assertEquals(user.getId().toString(), claims.get("userId", String.class));
        assertEquals("ROLE_" + user.getRole().toString(), claims.get("roles", String.class));
        assertTrue(claims.getExpiration().after(new Date()));
        assertFalse(token.isEmpty());
    }
}