package com.adelok.marketplace_auth.controller;

import com.adelok.marketplace_auth.dto.*;
import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.security.CustomUserDetails;
import com.adelok.marketplace_auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateTradelink(
            @RequestBody
            @Valid UpdateProfileRequest request
    ) {
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(authService.updateTradeLink(user, request.tradeLink()));

    }
}
