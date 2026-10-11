package com.adelok.marketplace_auth.controller;

import com.adelok.marketplace_auth.dto.LoginRequest;
import com.adelok.marketplace_auth.dto.LoginResponse;
import com.adelok.marketplace_auth.dto.RefreshRequest;
import com.adelok.marketplace_auth.dto.RefreshResponse;
import com.adelok.marketplace_auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginOrRegisterBySteamId(
            @RequestBody
            @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(authService.loginOrRegisterBySteamId(request.steamId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(
            @RequestBody
            @Valid RefreshRequest request
    ) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }
}
