package com.desafio.hotel.services.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.desafio.hotel.entity.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Implementação do serviço de gerenciamento de tokens JWT.
 *
 * <p>Responsável por gerar e validar tokens JWT usando a biblioteca Auth0.
 * Os tokens possuem validade de 2 horas após sua geração.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Service
public class LocalTokenServiceImpl implements LocalTokenService {
    /** Chave secreta para assinatura e validação de tokens */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um novo token JWT para um usuário.
     *
     * <p>O token é assinado com HMAC256 e contém informações do usuário.
     * Válido por 2 horas.</p>
     *
     * @param user usuário para o qual gerar o token
     * @return token JWT gerado
     * @throws RuntimeException se houver erro na geração do token
     */
    @Override
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Hotel")
                    .withSubject(user.getLogin())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token",exception);
        }
    }

    /**
     * Valida um token JWT e extrai o login do usuário.
     *
     * @param token token JWT a ser validado
     * @return login do usuário contido no token
     * @throws RuntimeException se o token for inválido ou expirado
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Hotel")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while validating token",exception);
        }
    }

    /**
     * Gera a data de expiração do token.
     *
     * <p>Define o token para expirar 2 horas após a geração, no fuso horário -03:00.</p>
     *
     * @return Instant representando a data de expiração
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
