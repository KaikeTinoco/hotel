package com.desafio.hotel.services.tokens;

import com.desafio.hotel.entity.users.User;

public interface LocalTokenService {
    String generateToken(User user);
    String validateToken(String token);
}
