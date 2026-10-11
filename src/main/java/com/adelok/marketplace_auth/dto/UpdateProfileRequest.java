package com.adelok.marketplace_auth.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequest(
        @Pattern(regexp = "^$|^https://steamcommunity\\.com/tradeoffer/new/\\?partner=\\d+&token=[A-Za-z0-9_-]+$",
                message = "Invalid tradelink")
        String tradeLink
) {
}