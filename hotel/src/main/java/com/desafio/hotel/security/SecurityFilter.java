package com.desafio.hotel.security;

import com.desafio.hotel.repositories.UserRepository;
import com.desafio.hotel.services.tokens.LocalTokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança para validação de tokens JWT.
 *
 * <p>Executa uma vez por requisição HTTP para validar tokens JWT e
 * autenticar usuários baseado nas informações do token.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final LocalTokenServiceImpl localTokenService;
    private final UserRepository userRepository;

    @Autowired
    public SecurityFilter(LocalTokenServiceImpl localTokenService, UserRepository userRepository) {
        this.localTokenService = localTokenService;
        this.userRepository = userRepository;
    }

    /**
     * Executa a lógica de filtro de autenticação.
     *
     * <p>Valida o token JWT presente no header Authorization e,
     * se válido, autentica o usuário no contexto de segurança.</p>
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @param filterChain cadeia de filtros
     * @throws ServletException se houver erro no processamento
     * @throws IOException se houver erro de I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var login = localTokenService.validateToken(token);
            UserDetails user = userRepository.findByLogin(login);
            var authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }

    /**
     * Extrai o token JWT do header Authorization.
     *
     * <p>Espera o formato "Bearer <token>".</p>
     *
     * @param request requisição HTTP
     * @return token extraído ou null se não encontrado
     */
    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            return null;
        }
        return header.replace("Bearer ","").trim();
    }
}
