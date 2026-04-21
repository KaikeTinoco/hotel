package com.desafio.hotel.dto.auth;

/**
 * Data Transfer Object para autenticação de usuários.
 *
 * <p>Representa os dados necessários para fazer login na API:
 * login (nome de usuário) e senha.</p>
 *
 * @param login nome de usuário para autenticação
 * @param password senha do usuário
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
public record AuthDTO(
        String password,
        String login
) {
}
