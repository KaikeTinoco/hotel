package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * Repositório para persistência de dados de usuários.
 *
 * <p>Fornece operações CRUD e autenticação para a entidade User,
 * integrado com Spring Security.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository  extends JpaRepository<User, String> {
    /**
     * Busca um usuário pelo seu login (nome de usuário).
     *
     * @param login nome de usuário
     * @return UserDetails do usuário se encontrado, null caso contrário
     */
    UserDetails findByLogin(String login);
}
