package com.desafio.hotel.dto.auth;

import com.desafio.hotel.entity.users.UserRole;

/**
 * Data Transfer Object para registro de novos usuários.
 *
 * <p>Contém os dados necessários para registrar um novo usuário no sistema:
 * login, senha e função (role) do usuário.</p>
 *
 * @param login nome de usuário único para o sistema
 * @param password senha do usuário (será criptografada no servidor)
 * @param role função do usuário (USER ou ADMIN)
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
public record RegisterDTO(String login, String password, UserRole role) {
}
