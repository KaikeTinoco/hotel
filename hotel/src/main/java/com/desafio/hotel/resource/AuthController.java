package com.desafio.hotel.resource;

import com.desafio.hotel.dto.auth.AuthDTO;
import com.desafio.hotel.dto.auth.RegisterDTO;
import com.desafio.hotel.services.security.AuthServiceImpl;
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

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    private final AuthServiceImpl authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthServiceImpl authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("register")
    public ResponseEntity registerUser(@RequestBody @Valid RegisterDTO authDTO) {
        authService.register(authDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
