package com.desafio.hotel.services.security;

import com.desafio.hotel.dto.auth.RegisterDTO;
import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.entity.users.UserRole;
import com.desafio.hotel.exceptions.UserLoginAlreadyTaken;
import com.desafio.hotel.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o serviço de autenticação de usuários.
 *
 * <p>Verifica a funcionalidade de carregamento de usuários, registro de novos
 * usuários e tratamento de exceções de duplicação de login.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User usuario;
    private RegisterDTO registroDTO;

    @BeforeEach
    void inicializar() {
        MockitoAnnotations.openMocks(this);

        usuario = User.builder()
                .id("123")
                .login("usuario_teste")
                .password("$2a$10$senhaencriptada.abcd1234.bcrypt")
                .role(UserRole.USER)
                .build();

        registroDTO = new RegisterDTO(
                "novo_usuario",
                "senha_nova",
                UserRole.USER
        );
    }

    @Test
    void carregarUsuarioPorUsernameSucesso() {
        // Arrange
        when(userRepository.findByLogin("usuario_teste"))
                .thenReturn(usuario);

        // Act
        UserDetails usuarioCarregado = authService.loadUserByUsername("usuario_teste");

        // Assert
        assertNotNull(usuarioCarregado, "O usuário deve ser carregado");
        assertEquals("usuario_teste", usuarioCarregado.getUsername(),
                "O username deve ser 'usuario_teste'");
        verify(userRepository, times(1)).findByLogin("usuario_teste");
    }

    @Test
    void carregarUsuarioPorUsernameNaoEncontrado() {
        // Arrange
        when(userRepository.findByLogin(anyString()))
                .thenReturn(null);

        // Act
        UserDetails usuarioCarregado = authService.loadUserByUsername("usuario_inexistente");

        // Assert
        assertNull(usuarioCarregado, "Não deve encontrar usuário inexistente");
        verify(userRepository, times(1)).findByLogin("usuario_inexistente");
    }

    @Test
    void registrarNovoUsuarioComSucesso() {
        // Arrange
        when(userRepository.findByLogin(registroDTO.login()))
                .thenReturn(null);
        when(userRepository.save(any(User.class)))
                .thenReturn(usuario);

        // Act
        authService.register(registroDTO);

        // Assert
        ArgumentCaptor<User> usuarioCapturado = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(usuarioCapturado.capture());

        User usuarioSalvo = usuarioCapturado.getValue();
        assertEquals("novo_usuario", usuarioSalvo.getLogin(),
                "O login deve ser 'novo_usuario'");
        assertEquals(UserRole.USER, usuarioSalvo.getRole(),
                "A role deve ser USER");
    }

    @Test
    void registrarUsuarioComLoginJaExistente() {
        // Arrange
        when(userRepository.findByLogin("usuario_teste"))
                .thenReturn(usuario);

        RegisterDTO registroDuplicado = new RegisterDTO(
                "usuario_teste",
                "senha_qualquer",
                UserRole.USER
        );

        // Act & Assert
        assertThrows(UserLoginAlreadyTaken.class, () -> authService.register(registroDuplicado),
                "Deve lançar exceção ao registrar usuário com login já existente");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void senhaDoNovoUsuarioEstaEncriptada() {
        // Arrange
        when(userRepository.findByLogin(registroDTO.login()))
                .thenReturn(null);
        when(userRepository.save(any(User.class)))
                .thenReturn(usuario);

        // Act
        authService.register(registroDTO);

        // Assert
        ArgumentCaptor<User> usuarioCapturado = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(usuarioCapturado.capture());

        User usuarioSalvo = usuarioCapturado.getValue();
        String senhaEncriptada = usuarioSalvo.getPassword();

        assertNotNull(senhaEncriptada, "A senha não pode ser nula");
        assertNotEquals("senha_nova", senhaEncriptada,
                "A senha não pode ser armazenada em texto plano");
        assertTrue(senhaEncriptada.startsWith("$2a$") || senhaEncriptada.startsWith("$2b$"),
                "A senha deve estar em formato BCrypt");
    }

    @Test
    void registrarUsuarioComRoleAdmin() {
        // Arrange
        RegisterDTO registroAdmin = new RegisterDTO(
                "admin_usuario",
                "senha_admin",
                UserRole.ADMIN
        );

        when(userRepository.findByLogin("admin_usuario"))
                .thenReturn(null);
        when(userRepository.save(any(User.class)))
                .thenReturn(usuario);

        // Act
        authService.register(registroAdmin);

        // Assert
        ArgumentCaptor<User> usuarioCapturado = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(usuarioCapturado.capture());

        User usuarioSalvo = usuarioCapturado.getValue();
        assertEquals(UserRole.ADMIN, usuarioSalvo.getRole(),
                "O usuário deve ter role ADMIN");
    }

    @Test
    void carregarUsuarioVariasVezes() {
        // Arrange
        when(userRepository.findByLogin("usuario_teste"))
                .thenReturn(usuario);

        // Act
        authService.loadUserByUsername("usuario_teste");
        authService.loadUserByUsername("usuario_teste");
        authService.loadUserByUsername("usuario_teste");

        // Assert
        verify(userRepository, times(3)).findByLogin("usuario_teste");
    }

    @Test
    void usuarioCarregadoTemAutoridades() {
        // Arrange
        when(userRepository.findByLogin("usuario_teste"))
                .thenReturn(usuario);

        // Act
        UserDetails usuarioCarregado = authService.loadUserByUsername("usuario_teste");

        // Assert
        assertNotNull(usuarioCarregado.getAuthorities(),
                "O usuário carregado deveria ter autoridades");
        assertFalse(usuarioCarregado.getAuthorities().isEmpty(),
                "As autoridades não podem estar vazias");
    }

    @Test
    void verificarIntegracaoBCryptComRegistro() {
        // Arrange
        RegisterDTO registroComSenhaForte = new RegisterDTO(
                "usuario_securo",
                "senha_muito_secura_123!@#",
                UserRole.USER
        );

        when(userRepository.findByLogin("usuario_securo"))
                .thenReturn(null);
        when(userRepository.save(any(User.class)))
                .thenReturn(usuario);

        // Act
        authService.register(registroComSenhaForte);

        // Assert
        ArgumentCaptor<User> usuarioCapturado = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(usuarioCapturado.capture());

        User usuarioSalvo = usuarioCapturado.getValue();
        String senhaEncriptada = usuarioSalvo.getPassword();

        // Verificar se a senha encriptada pode ser validada
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("senha_muito_secura_123!@#", senhaEncriptada),
                "A senha encriptada deve corresponder à senha original quando verificada");
    }

    @Test
    void registrarMultiplosUsuariosComSucesso() {
        // Arrange
        RegisterDTO usuario1 = new RegisterDTO("usuario1", "senha1", UserRole.USER);
        RegisterDTO usuario2 = new RegisterDTO("usuario2", "senha2", UserRole.ADMIN);

        when(userRepository.findByLogin("usuario1")).thenReturn(null);
        when(userRepository.findByLogin("usuario2")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(usuario);

        // Act
        authService.register(usuario1);
        authService.register(usuario2);

        // Assert
        verify(userRepository, times(2)).findByLogin(anyString());
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void senhasDiferentesGeramHashDiferentes() {
        // Arrange
        RegisterDTO usuario1 = new RegisterDTO("usuario1", "senha_um", UserRole.USER);
        RegisterDTO usuario2 = new RegisterDTO("usuario2", "senha_dois", UserRole.USER);

        when(userRepository.findByLogin("usuario1")).thenReturn(null);
        when(userRepository.findByLogin("usuario2")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(usuario);

        // Act
        authService.register(usuario1);
        authService.register(usuario2);

        // Assert
        ArgumentCaptor<User> usuarioCapturado = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(usuarioCapturado.capture());

        var usuarios = usuarioCapturado.getAllValues();
        String hash1 = usuarios.get(0).getPassword();
        String hash2 = usuarios.get(1).getPassword();

        assertNotEquals(hash1, hash2, "Senhas diferentes devem gerar hashes diferentes");
    }
}

