package com.desafio.hotel.services.security;

import com.desafio.hotel.dto.auth.RegisterDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interface para serviço de autenticação de usuários.
 *
 * <p>Estende UserDetailsService do Spring Security e fornece operações
 * para carregar usuários e registrar novos usuários no sistema.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
public interface AuthService extends UserDetailsService {
    /**
     * Carrega um usuário pelo nome de usuário (login).
     *
     * @param username nome de usuário a ser carregado
     * @return detalhes do usuário se encontrado
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Registra um novo usuário no sistema.
     *
     * @param dto objeto contendo os dados do usuário a registrar (login, senha e função)
     */
    void register(RegisterDTO dto);
}
