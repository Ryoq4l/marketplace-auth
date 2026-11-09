package com.adelok.marketplace_auth.controller;

import com.adelok.marketplace_auth.dto.LoginResponse;
import com.adelok.marketplace_auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2SuccessController {

    private final AuthService authService;

    public OAuth2SuccessController(AuthService authService) {
        this.authService = authService;
    }

    // ВАЖНО: Путь без /api/v1/auth, так как Gateway отрезает его через StripPrefix=3
    @GetMapping("/oauth-success")
    public ResponseEntity<LoginResponse> oauthSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            System.out.println("=== ERROR: oauth2User is NULL ===");
            throw new IllegalStateException("No OAuth2 user found");
        }

        System.out.println("=== OAUTH2 USER ATTRIBUTES ===");
        oauth2User.getAttributes().forEach((key, value) ->
                System.out.println(key + " : " + value)
        );
        System.out.println("=============================");

        String steamId = null;
        if (oauth2User.getAttribute("sub") != null) {
            steamId = oauth2User.getAttribute("sub");
        } else if (oauth2User.getAttribute("steamid") != null) {
            steamId = oauth2User.getAttribute("steamid");
        } else if (oauth2User.getAttribute("id") != null) {
            steamId = String.valueOf(oauth2User.getAttribute("id"));
        }

        if (steamId == null) {
            System.out.println("=== ERROR: steamId is NULL. Check attributes above! ===");
            throw new IllegalStateException("Steam ID is null");
        }

        LoginResponse response = authService.loginOrRegisterBySteamId(steamId);
        return ResponseEntity.ok(response);
    }
}