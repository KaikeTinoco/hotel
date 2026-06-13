package com.desafio.hotel.resource;

import com.desafio.hotel.dto.auth.AuthDTO;
import com.desafio.hotel.dto.auth.LoginResponseDTO;
import com.desafio.hotel.dto.auth.RegisterDTO;
import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.entity.users.UserRole;
import com.desafio.hotel.exceptions.UserLoginAlreadyTaken;
import com.desafio.hotel.services.security.AuthServiceImpl;
import com.desafio.hotel.services.tokens.LocalTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes para o controlador de autenticação.
 *
 * <p>Verifica os endpoints de login e registro de usuários,
 * incluindo geração de tokens JWT e tratamento de erros.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LocalTokenServiceImpl localTokenService;

    private User usuario;
    private AuthDTO authDTO;
    private RegisterDTO registroDTO;
    private static final String TOKEN_TESTE = "token_teste_jwt_aqui";

    @BeforeEach
    void inicializar(){
        MockitoAnnotations.openMocks(this);

        usuario = User.builder()
                .id("123")
                .login("usuario_teste")
                .password("$2a$10$senhaencriptada.test")
                .role(UserRole.USER)
                .build();

        authDTO = new AuthDTO("usuario_teste", "senha_teste");

        registroDTO = new RegisterDTO(
                "novo_usuario",
                "senha_nova_teste",
                UserRole.USER
        );
    }

    @Test
    void loginComSucesso() {
        // Arrange
        Authentication autenticacao = mock(Authentication.class);
        when(autenticacao.getPrincipal()).thenReturn(usuario);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(autenticacao);
        when(localTokenService.generateToken(usuario))
                .thenReturn(TOKEN_TESTE);

        // Act
        ResponseEntity<?> response = authController.login(authDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
        assertInstanceOf(LoginResponseDTO.class, response.getBody(),
                "A resposta deve ser do tipo LoginResponseDTO");

        LoginResponseDTO loginResponse = (LoginResponseDTO) response.getBody();
        assertEquals(TOKEN_TESTE, loginResponse.token(),
                "O token deve corresponder ao gerado");
    }

    @Test
    void loginComCredenciaisInvalidas() {
        // Arrange
        AuthDTO credenciaisInvalidas = new AuthDTO("usuario_invalido", "senha_errada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException(
                        "Credenciais inválidas"));

        // Act & Assert
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> authController.login(credenciaisInvalidas),
                "Deve lançar exceção para credenciais inválidas");
    }

    @Test
    void registrarNovoUsuarioComSucesso() {
        // Arrange
        doNothing().when(authService).register(registroDTO);

        // Act
        ResponseEntity<?> response = authController.registerUser(registroDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "O status deve ser CREATED");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
        assertInstanceOf(String.class, response.getBody(),
                "A resposta deve ser uma mensagem de texto");

        verify(authService, times(1)).register(registroDTO);
    }

    @Test
    void registrarUsuarioComLoginJaExistente() {
        // Arrange
        RegisterDTO usuarioDuplicado = new RegisterDTO(
                "usuario_existente",
                "senha",
                UserRole.USER
        );

        doThrow(new UserLoginAlreadyTaken("User login already exists"))
                .when(authService).register(usuarioDuplicado);

        // Act & Assert
        assertThrows(UserLoginAlreadyTaken.class,
                () -> authController.registerUser(usuarioDuplicado),
                "Deve lançar exceção para login duplicado");
    }

    @Test
    void registrarUsuarioComRoleAdmin() {
        // Arrange
        RegisterDTO registroAdmin = new RegisterDTO(
                "novo_admin",
                "senha_admin",
                UserRole.ADMIN
        );

        doNothing().when(authService).register(registroAdmin);

        // Act
        ResponseEntity<?> response = authController.registerUser(registroAdmin);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "O status deve ser CREATED");
        verify(authService, times(1)).register(registroAdmin);
    }

    @Test
    void loginGeraNovTokenACadaRequisicao() {
        // Arrange
        String token1 = "token_1_teste";
        String token2 = "token_2_teste";

        Authentication autenticacao1 = mock(Authentication.class);
        when(autenticacao1.getPrincipal()).thenReturn(usuario);

        Authentication autenticacao2 = mock(Authentication.class);
        when(autenticacao2.getPrincipal()).thenReturn(usuario);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(autenticacao1)
                .thenReturn(autenticacao2);

        when(localTokenService.generateToken(usuario))
                .thenReturn(token1)
                .thenReturn(token2);

        // Act
        ResponseEntity<?> response1 = authController.login(authDTO);
        ResponseEntity<?> response2 = authController.login(authDTO);

        // Assert
        LoginResponseDTO loginResponse1 = (LoginResponseDTO) response1.getBody();
        LoginResponseDTO loginResponse2 = (LoginResponseDTO) response2.getBody();

        assertNotNull(loginResponse1, "Primeira resposta não pode ser nula");
        assertNotNull(loginResponse2, "Segunda resposta não pode ser nula");

        assertEquals(token1, loginResponse1.token(),
                "O primeiro token deve corresponder");
        assertEquals(token2, loginResponse2.token(),
                "O segundo token deve corresponder");
        assertNotEquals(token1, token2,
                "Cada requisição deve gerar um novo token");
    }

    @Test
    void registrarVariosUsuariosComSucesso() {
        // Arrange
        RegisterDTO usuario1 = new RegisterDTO("usuario1", "senha1", UserRole.USER);
        RegisterDTO usuario2 = new RegisterDTO("usuario2", "senha2", UserRole.ADMIN);

        doNothing().when(authService).register(usuario1);
        doNothing().when(authService).register(usuario2);

        // Act
        ResponseEntity<?> response1 = authController.registerUser(usuario1);
        ResponseEntity<?> response2 = authController.registerUser(usuario2);

        // Assert
        assertEquals(HttpStatus.CREATED, response1.getStatusCode(),
                "Primeiro registro deve retornar CREATED");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode(),
                "Segundo registro deve retornar CREATED");
        verify(authService, times(2)).register(any(RegisterDTO.class));
    }

    @Test
    void loginComUsuarioComAutorizacoesCompletas() {
        // Arrange
        User usuarioAdmin = User.builder()
                .id("456")
                .login("admin_usuario")
                .password("$2a$10$senhaadmin")
                .role(UserRole.ADMIN)
                .build();

        Authentication autenticacao = mock(Authentication.class);
        when(autenticacao.getPrincipal()).thenReturn(usuarioAdmin);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(autenticacao);
        when(localTokenService.generateToken(usuarioAdmin))
                .thenReturn(TOKEN_TESTE);

        // Act
        ResponseEntity<?> response = authController.login(authDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        verify(localTokenService, times(1)).generateToken(usuarioAdmin);
    }

    @Test
    void registrarUsuarioComSenhaCompleta() {
        // Arrange
        RegisterDTO usuarioComSenhaCompleta = new RegisterDTO(
                "usuario_seguro",
                "Senha@Forte#2025!",
                UserRole.USER
        );

        doNothing().when(authService).register(usuarioComSenhaCompleta);

        // Act
        ResponseEntity<?> response = authController.registerUser(usuarioComSenhaCompleta);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "O status deve ser CREATED");
        verify(authService, times(1)).register(usuarioComSenhaCompleta);
    }

    @Test
    void loginRetornaTokenJWT() {
        // Arrange
        Authentication autenticacao = mock(Authentication.class);
        when(autenticacao.getPrincipal()).thenReturn(usuario);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(autenticacao);
        when(localTokenService.generateToken(usuario))
                .thenReturn(TOKEN_TESTE);

        // Act
        ResponseEntity<?> response = authController.login(authDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O token não pode ser nulo");
    }

    @Test
    void registroRetornaMensagemDeSucesso() {
        // Arrange
        doNothing().when(authService).register(registroDTO);

        // Act
        ResponseEntity<?> response = authController.registerUser(registroDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "O status deve ser CREATED");
        assertEquals("User registered successfully", response.getBody(),
                "A mensagem de sucesso deve ser a esperada");
    }
}


