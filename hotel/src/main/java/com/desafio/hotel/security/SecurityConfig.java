package com.desafio.hotel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança da aplicação.
 *
 * <p>Define políticas de autenticação, autorização e gerenciamento de sessão
 * usando Spring Security com JWT (JSON Web Tokens).</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SecurityFilter securityFilter;

    @Autowired
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    /**
     * Configura a cadeia de filtros de segurança HTTP.
     *
     * <p>Define quais endpoints requerem autenticação e autorização específica.</p>
     *
     * @param http objeto HttpSecurity para configuração
     * @return SecurityFilterChain configurada
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/chekcin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/chekcin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/chekcin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/checkout/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/checkout/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/checkout/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/guests/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/guests/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/guests/**").hasRole("ADMIN")
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configura o gerenciador de autenticação.
     *
     * @param authenticationConfiguration configuração de autenticação
     * @return AuthenticationManager configurado
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura o codificador de senhas usando BCrypt.
     *
     * @return PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
