package com.desafio.hotel.services.tokens;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.entity.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o serviço de geração e validação de tokens JWT.
 *
 * <p>Verifica a funcionalidade de geração de tokens JWT válidos,
 * validação de tokens e tratamento de exceções.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class LocalTokenServiceImplTest {
    private LocalTokenServiceImpl tokenService;
    private User usuario;
    private static final String CHAVE_SECRETA = "chave-super-secreta-para-testes-de-jwt";

    @BeforeEach
    void inicializar() {
        tokenService = new LocalTokenServiceImpl();
        ReflectionTestUtils.setField(tokenService, "secret", CHAVE_SECRETA);

        usuario = User.builder()
                .id("123")
                .login("usuario_teste")
                .password("senha_criptografada")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void gerarTokenComSucesso() {
        // Arrange
        // Preparado no BeforeEach

        // Act
        String token = tokenService.generateToken(usuario);

        // Assert
        assertNotNull(token, "O token não pode ser nulo");
        assertFalse(token.isEmpty(), "O token não pode estar vazio");
        assertTrue(token.contains("."), "O token JWT deve conter pontos");
    }

    @Test
    void gerarTokenComUsuarioDiferentes() {
        // Arrange
        User outroUsuario = User.builder()
                .id("456")
                .login("outro_usuario")
                .password("outra_senha")
                .role(UserRole.ADMIN)
                .build();

        // Act
        String token1 = tokenService.generateToken(usuario);
        String token2 = tokenService.generateToken(outroUsuario);

        // Assert
        assertNotEquals(token1, token2, "Tokens de usuários diferentes devem ser diferentes");
    }

    @Test
    void validarTokenComSucesso() {
        // Arrange
        String token = tokenService.generateToken(usuario);

        // Act
        String loginExtraido = tokenService.validateToken(token);

        // Assert
        assertEquals(usuario.getLogin(), loginExtraido,
                "O login extraído do token deve ser igual ao login do usuário");
    }

    @Test
    void validarTokenInvalidoLancaExcecao() {
        // Arrange
        String tokenInvalido = "token.invalido.aqui";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.validateToken(tokenInvalido),
                "Deve lançar RuntimeException ao validar token inválido");
    }

    @Test
    void validarTokenComChaveSecretaDiferenteLancaExcecao() {
        // Arrange
        String token = tokenService.generateToken(usuario);

        // Mudar a chave secreta para simular chave diferente
        ReflectionTestUtils.setField(tokenService, "secret", "outra-chave-diferente");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.validateToken(token),
                "Deve lançar RuntimeException ao validar token com chave secreta diferente");
    }

    @Test
    void validarTokenNuloLancaExcecao() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.validateToken(null),
                "Deve lançar RuntimeException ao validar token nulo");
    }

    @Test
    void gerarTokenComUsuarioNuloLancaExcecao() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.generateToken(null),
                "Deve lançar RuntimeException ao gerar token com usuário nulo");
    }

    @Test
    void tokenGeradoTemFormatoCorreto() {
        // Arrange & Act
        String token = tokenService.generateToken(usuario);

        // Assert
        String[] partes = token.split("\\.");
        assertEquals(3, partes.length, "Um token JWT deve ter exatamente 3 partes separadas por pontos");
    }

    @Test
    void extrairLoginDoTokenGerado() {
        // Arrange
        User usuarioComLoginEspecifico = User.builder()
                .id("789")
                .login("joao_silva")
                .password("senha")
                .role(UserRole.USER)
                .build();

        // Act
        String token = tokenService.generateToken(usuarioComLoginEspecifico);
        String loginExtraido = tokenService.validateToken(token);

        // Assert
        assertEquals("joao_silva", loginExtraido,
                "Deve extrair corretamente o login do token");
    }
}