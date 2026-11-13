package com.adelok.marketplace_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @Pattern(regexp = "^\\d{17}$", message = "Invalid steamId format")
        @NotBlank
        String steamId
) {
}
