package com.desafio.hotel.handler;

import com.desafio.hotel.dto.error.ErrorResponseDTO;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.exceptions.GuestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GuestNotFoundException.class)
    private ResponseEntity<?> guestNotFoundException(GuestNotFoundException exception){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<?> badRequestException(BadRequestException exception){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> exception(Exception e){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
