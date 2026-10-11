package com.adelok.marketplace_auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String steamId
) {
}
