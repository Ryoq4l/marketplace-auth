package com.adelok.marketplace_auth.service;

import com.adelok.marketplace_auth.dto.LoginResponse;
import com.adelok.marketplace_auth.dto.RefreshResponse;
import com.adelok.marketplace_auth.dto.UserResponse;
import com.adelok.marketplace_auth.entity.RefreshToken;
import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.repository.UserRepository;
import com.adelok.marketplace_auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public LoginResponse loginOrRegisterBySteamId(String steamId) {
        if (steamId == null || steamId.isBlank()) throw new IllegalArgumentException("steamid is required");
        User user = userRepository.findBySteamId(steamId)
                .orElseGet(() -> userRepository.saveAndFlush(User.builder().steamId(steamId).build()));
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.create(user);
        UserResponse userResponse = new UserResponse(user.getId(), user.getSteamId(), user.getRole());
        return new LoginResponse(accessToken, refreshToken, userResponse);
    }

    @Transactional(readOnly = true)
    public RefreshResponse refresh(String refreshToken) {
        RefreshToken token = refreshTokenService.validate(refreshToken);
        User user = token.getUser();
        String access = jwtService.generateAccessToken(user);
        return new RefreshResponse(access);
    }
}
