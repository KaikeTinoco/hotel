package com.desafio.hotel.exceptions;

public class UserLoginAlreadyTaken extends RuntimeException {
    public UserLoginAlreadyTaken(String message) {
        super(message);
    }
}
