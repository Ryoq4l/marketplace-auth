package com.adelok.marketplace_auth.security;

import com.adelok.marketplace_auth.config.JwtProperties;
import com.adelok.marketplace_auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private SecretKey signingKey;

    @PostConstruct
    public void initSigningKey() {
          this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getSteamId())
                .claim("userId", user.getId().toString())
                .claim("roles", "ROLE_" + user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .signWith(signingKey)
                .compact();
    }

    //TODO: Move to refresh token service
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}
