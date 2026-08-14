package com.healthpay.auth_service.presentation.dto;

import com.healthpay.auth_service.domain.UserRole;

public record RegisterRequestDTO(String email, String password, UserRole role) {
}
