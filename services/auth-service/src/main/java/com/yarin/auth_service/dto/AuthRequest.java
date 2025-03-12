package com.yarin.auth_service.dto;

public record AuthRequest(
        String username,
        String password
) {
}
