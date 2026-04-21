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

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final LocalTokenServiceImpl localTokenService;
    private final UserRepository userRepository;

    @Autowired
    public SecurityFilter(LocalTokenServiceImpl localTokenService, UserRepository userRepository) {
        this.localTokenService = localTokenService;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var login = localTokenService.validateToken(token);
            UserDetails user = userRepository.findByLogin(login);
            var authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            //verifica e salva o user no contexto da autenticação
            //se não encontrar token, só chama o próximo filtro
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //essa linha informa ao spring que o filtro já acabou e pode chamar o próximo filtro
        filterChain.doFilter(request,response);
    }

    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        //como estamos usando JWT Bearer para auth, o user é o Bearer do token
        if (header == null || !header.startsWith("Bearer")) {
            return null;
        }
        return header.replace("Bearer ","").trim();
    }
}
