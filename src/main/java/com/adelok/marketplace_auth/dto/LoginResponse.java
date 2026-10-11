package com.adelok.marketplace_auth.dto;

public record LoginResponse (
String accessToken,
String refreshToken,
UserResponse user
){
}
