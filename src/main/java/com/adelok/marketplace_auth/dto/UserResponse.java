package com.adelok.marketplace_auth.dto;

import com.adelok.marketplace_auth.entity.enumerated.Role;

import java.util.UUID;

public record UserResponse (
        UUID id,
        String steamId,
        Role role,
        String tradeLink
){
}
