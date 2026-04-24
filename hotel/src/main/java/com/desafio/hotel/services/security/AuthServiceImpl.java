package com.desafio.hotel.services.security;

import com.desafio.hotel.dto.auth.RegisterDTO;
import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.exceptions.UserLoginAlreadyTaken;
import com.desafio.hotel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de autenticação de usuários.
 *
 * <p>Responsável por gerenciar o carregamento de usuários e registro de novos
 * usuários no sistema com criptografia de senha.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carrega um usuário pelo nome de usuário (login).
     *
     * @param username nome de usuário a ser carregado
     * @return detalhes do usuário ou null se não encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    /**
     * Registra um novo usuário no sistema com senha criptografada.
     *
     * @param dto objeto contendo login, senha e função do usuário
     * @throws UserLoginAlreadyTaken se o login já existe no sistema
     */
    @Override
    public void register(RegisterDTO dto) {
        if (this.userRepository.findByLogin(dto.login()) != null) {
            throw new UserLoginAlreadyTaken("User login already exists");
        }

        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = User.builder()
                .login(dto.login())
                    .password(encriptedPassword)
                        .role(dto.role())
                                .build();

        userRepository.save(user);
    }
}
