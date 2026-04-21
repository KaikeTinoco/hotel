package com.desafio.hotel.resource;

import com.desafio.hotel.dto.auth.LoginResponseDTO;
import com.desafio.hotel.dto.auth.AuthDTO;
import com.desafio.hotel.dto.auth.RegisterDTO;
import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.services.security.AuthServiceImpl;
import com.desafio.hotel.services.tokens.LocalTokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para gerenciamento de autenticação de usuários.
 *
 * <p>Fornece endpoints para login e registro de novos usuários na API.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    private final AuthServiceImpl authService;
    private final AuthenticationManager authenticationManager;
    private final LocalTokenServiceImpl localTokenService;

    @Autowired
    public AuthController(AuthServiceImpl authService, AuthenticationManager authenticationManager, LocalTokenServiceImpl localTokenService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.localTokenService = localTokenService;
    }

    /**
     * Autentica um usuário e retorna um token JWT.
     *
     * @param authDTO credenciais do usuário (login e senha)
     * @return ResponseEntity com token JWT se autenticação for bem-sucedida
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = localTokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * @param authDTO dados do novo usuário (login, senha e função)
     * @return ResponseEntity com status 201 se registro for bem-sucedido
     */
    @PostMapping("register")
    public ResponseEntity registerUser(@RequestBody @Valid RegisterDTO authDTO) {
        authService.register(authDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
