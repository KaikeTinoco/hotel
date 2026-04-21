package com.desafio.hotel.services.security;

import com.desafio.hotel.dto.auth.RegisterDTO;
import com.desafio.hotel.entity.users.User;
import com.desafio.hotel.exceptions.UserLoginAlreadyTaken;
import com.desafio.hotel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    @Override
    public void register(RegisterDTO dto) {
        if (this.userRepository.findByLogin(dto.login()) != null) {
            throw new UserLoginAlreadyTaken("User login already exists");
        }

        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = User.builder()
                .login(dto.login())
                    .password(encriptedPassword)
                        .role(dto.role())
                                .build();

        userRepository.save(user);
    }
}
