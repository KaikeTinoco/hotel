package com.desafio.hotel.entity.users;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade que representa um usuário do sistema.
 *
 * <p>Implementa UserDetails do Spring Security para autenticação e autorização.
 * Armazena credenciais de login, senha criptografada e função do usuário.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    /** Identificador único do usuário (UUID) */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Nome de login do usuário */
    private String login;

    /** Senha criptografada do usuário */
    private String password;

    /** Função do usuário (USER ou ADMIN) */
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * Retorna as autoridades (permissões) do usuário.
     *
     * <p>Usuários ADMIN recebem ambas as roles ROLE_ADMIN e ROLE_USER,
     * enquanto usuários normais recebem apenas ROLE_USER.</p>
     *
     * @return coleção de autoridades concedidas ao usuário
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return senha criptografada
     */
    @Override
    public @Nullable String getPassword() {
         return this.password;
    }

    /**
     * Retorna o nome de usuário (login).
     *
     * @return login do usuário
     */
    @Override
    public String getUsername() {
        return this.login;
    }

    /**
     * Verifica se a conta não está expirada.
     *
     * @return true, conta nunca expira
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Verifica se a conta não está bloqueada.
     *
     * @return true, conta nunca é bloqueada
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
}
