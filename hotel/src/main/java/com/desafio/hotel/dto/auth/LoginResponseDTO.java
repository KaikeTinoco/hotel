package com.desafio.hotel.dto.auth;

/**
 * Data Transfer Object para resposta de login.
 *
 * <p>Retorna o token JWT gerado após autenticação bem-sucedida.</p>
 *
 * @param token token JWT para autenticação em requisições subsequentes
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
public record LoginResponseDTO(String token) {
}
