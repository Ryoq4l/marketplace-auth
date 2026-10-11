package com.adelok.marketplace_auth.service;

import com.adelok.marketplace_auth.config.JwtProperties;
import com.adelok.marketplace_auth.entity.RefreshToken;
import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.exceptions.InvalidTokenException;
import com.adelok.marketplace_auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public String create(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getRefreshTokenExpiration()));
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(expiresAt)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found"));
        if (refreshToken.isRevoked()) throw new InvalidTokenException("Token has been revoked");
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new InvalidTokenException("Refresh token is expired");
        return refreshToken;
    }

    @Transactional
    public void revoke(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

    }

    @Transactional
    public void revokeAllForUser(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUser(user);
        for (RefreshToken token : tokens) {
            token.setRevoked(true);
        }
        refreshTokenRepository.saveAll(tokens);
    }
}
