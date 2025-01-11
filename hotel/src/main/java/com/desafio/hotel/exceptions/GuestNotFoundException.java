package com.desafio.hotel.exceptions;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String message) {
        super(message);
    }
}
