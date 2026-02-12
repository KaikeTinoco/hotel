package com.desafio.hotel.dto.auth;

import com.desafio.hotel.entity.security.roles.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    private String login;
    private String password;
    private UserRoles role;
}
