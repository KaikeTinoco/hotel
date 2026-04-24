package com.desafio.hotel.services.tokens;

import com.desafio.hotel.entity.users.User;

/**
 * Interface para gerenciamento de tokens JWT.
 *
 * <p>Fornece operações para gerar e validar tokens JWT usados na autenticação
 * de usuários na API.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
public interface LocalTokenService {
    /**
     * Gera um novo token JWT para um usuário.
     *
     * @param user usuário para o qual gerar o token
     * @return token JWT gerado
     */
    String generateToken(User user);

    /**
     * Valida e extrai o login de um token JWT.
     *
     * @param token token JWT a ser validado
     * @return login do usuário extraído do token
     * @throws RuntimeException se o token for inválido ou expirado
     */
    String validateToken(String token);
}
