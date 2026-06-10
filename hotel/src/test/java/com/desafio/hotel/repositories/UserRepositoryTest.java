package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.entity.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para o repositório de usuários.
 *
 * <p>Verifica as operações de persistência de dados da entidade User,
 * incluindo consultas por login e operações CRUD.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User usuario;

    @BeforeEach
    void inicializar() {
        usuario = User.builder()
                .login("usuario_teste")
                .password("$2a$10$senhaencriptada.hash.bcrypt")
                .role(UserRole.USER)
                .build();

        userRepository.save(usuario);
    }

    @Test
    void buscarUsuarioPorLoginComSucesso() {
        // Act
        UserDetails usuarioEncontrado = userRepository.findByLogin("usuario_teste");

        // Assert
        assertNotNull(usuarioEncontrado, "O usuário deve ser encontrado pelo login");
        assertEquals("usuario_teste", usuarioEncontrado.getUsername(),
                "O login do usuário encontrado deve ser 'usuario_teste'");
    }

    @Test
    void buscarUsuarioPorLoginNaoEncontrado() {
        // Act
        UserDetails usuarioEncontrado = userRepository.findByLogin("login_inexistente");

        // Assert
        assertNull(usuarioEncontrado, "Não deve encontrar usuário com login inexistente");
    }

    @Test
    void buscarUsuarioPorLoginRetornaUserDetails() {
        // Act
        UserDetails usuarioEncontrado = userRepository.findByLogin("usuario_teste");

        // Assert
        assertNotNull(usuarioEncontrado, "O usuário deve ser encontrado");
        assertThat(usuarioEncontrado).isInstanceOf(UserDetails.class);
    }

    @Test
    void salvarNovoUsuarioComSucesso() {
        // Arrange
        User novoUsuario = User.builder()
                .login("novo_usuario")
                .password("$2a$10$novohassbcrypt")
                .role(UserRole.ADMIN)
                .build();

        // Act
        User usuarioSalvo = userRepository.save(novoUsuario);

        // Assert
        assertNotNull(usuarioSalvo, "O usuário salvo não pode ser nulo");
        assertNotNull(usuarioSalvo.getId(), "O ID do usuário salvo deve ser gerado");
        assertEquals("novo_usuario", usuarioSalvo.getLogin(),
                "O login deve ser preservado após salvar");
    }

    @Test
    void buscarUsuarioPorIdComSucesso() {
        // Arrange
        String idUsuario = usuario.getId();

        // Act
        Optional<User> usuarioEncontrado = userRepository.findById(idUsuario);

        // Assert
        assertTrue(usuarioEncontrado.isPresent(), "O usuário deve ser encontrado pelo ID");
        assertEquals(usuario.getLogin(), usuarioEncontrado.get().getLogin(),
                "O login deve ser o mesmo");
    }

    @Test
    void buscarUsuarioPorIdNaoEncontrado() {
        // Act
        Optional<User> usuarioEncontrado = userRepository.findById("id-inexistente-12345");

        // Assert
        assertFalse(usuarioEncontrado.isPresent(), "Não deve encontrar usuário com ID inexistente");
    }

    @Test
    void deletarUsuarioComSucesso() {
        // Arrange
        String idUsuario = usuario.getId();

        // Act
        userRepository.delete(usuario);
        Optional<User> usuarioDeletado = userRepository.findById(idUsuario);

        // Assert
        assertFalse(usuarioDeletado.isPresent(), "O usuário não deve existir após ser deletado");
    }

    @Test
    void atualizarUsuarioComSucesso() {
        // Arrange
        usuario.setRole(UserRole.ADMIN);

        // Act
        User usuarioAtualizado = userRepository.save(usuario);

        // Assert
        assertEquals(UserRole.ADMIN, usuarioAtualizado.getRole(),
                "A role do usuário deve ser atualizada para ADMIN");
    }

    @Test
    void listarTodosOsUsuarios() {
        // Arrange - Criar um segundo usuário
        User segundoUsuario = User.builder()
                .login("segundo_usuario")
                .password("$2a$10$outrohassbcrypt")
                .role(UserRole.USER)
                .build();
        userRepository.save(segundoUsuario);

        // Act
        var todosOsUsuarios = userRepository.findAll();

        // Assert
        assertTrue(todosOsUsuarios.size() >= 2, "Deve haver pelo menos 2 usuários");
    }

    @Test
    void verificarTotalDeUsuariosNoRepositorio() {
        // Act
        long totalUsuarios = userRepository.count();

        // Assert
        assertTrue(totalUsuarios >= 1, "Deve haver pelo menos 1 usuário no repositório");
    }

    @Test
    void usuarioComRoleUSERTemAutorizacoesCorretas() {
        // Act
        UserDetails usuario = userRepository.findByLogin("usuario_teste");

        // Assert
        assertNotNull(usuario, "O usuário deve ser encontrado");
        assertFalse(usuario.getAuthorities().isEmpty(), "O usuário deve ter autoridades");
    }

    @Test
    void salvarMultiplosUsuariosComLoginsUnicos() {
        // Arrange
        User usuario1 = User.builder()
                .login("usuario_unico_1")
                .password("$2a$10$hash1")
                .role(UserRole.USER)
                .build();

        User usuario2 = User.builder()
                .login("usuario_unico_2")
                .password("$2a$10$hash2")
                .role(UserRole.ADMIN)
                .build();

        // Act
        userRepository.save(usuario1);
        userRepository.save(usuario2);

        UserDetails encontrado1 = userRepository.findByLogin("usuario_unico_1");
        UserDetails encontrado2 = userRepository.findByLogin("usuario_unico_2");

        // Assert
        assertNotNull(encontrado1, "Primeiro usuário deve ser encontrado");
        assertNotNull(encontrado2, "Segundo usuário deve ser encontrado");
        assertNotEquals(encontrado1.getUsername(), encontrado2.getUsername(),
                "Os logins devem ser diferentes");
    }
}