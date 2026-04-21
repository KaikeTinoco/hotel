package com.desafio.hotel.services.security;

import com.desafio.hotel.dto.auth.RegisterDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
    void register(RegisterDTO dto);


}
