package com.desafio.hotel.dto.auth;

import com.desafio.hotel.entity.users.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
