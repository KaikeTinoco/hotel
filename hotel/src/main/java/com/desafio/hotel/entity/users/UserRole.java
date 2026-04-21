package com.desafio.hotel.entity.users;

/**
 * Enumeração que define os papéis (roles) disponíveis para usuários do sistema.
 *
 * <p>Define dois níveis de permissão: USER (padrão) e ADMIN (administrador).</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
public enum UserRole {
    /** Usuário com permissões limitadas */
    USER("user"),

    /** Usuário com permissões administrativas completas */
    ADMIN("admin");

    /** Descrição textual da função */
    private final String role;

    /**
     * Construtor da enumeração.
     *
     * @param role descrição textual da função
     */
    UserRole(String role) {
        this.role = role;
    }
}
