package com.desafio.hotel.handler;

import com.desafio.hotel.dto.error.ErrorResponseDTO;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.exceptions.GuestNotFoundException;
import com.desafio.hotel.exceptions.UserLoginAlreadyTaken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Manipulador centralizado de exceções para a API.
 *
 * <p>Intercepta exceções lançadas em toda a aplicação e as converte em
 * respostas HTTP apropriadas com mensagens de erro formatadas.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Manipula exceções de hóspede não encontrado.
     *
     * @param exception exceção lançada quando um hóspede não é encontrado
     * @return resposta com status 404 e detalhes do erro
     */
    @ExceptionHandler(GuestNotFoundException.class)
    private ResponseEntity<?> guestNotFoundException(GuestNotFoundException exception){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    /**
     * Manipula exceções de requisição inválida.
     *
     * @param exception exceção lançada para erros de validação ou requisição inválida
     * @return resposta com status 400 e detalhes do erro
     */
    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<?> badRequestException(BadRequestException exception){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula exceções genéricas não capturadas.
     *
     * @param e exceção genérica
     * @return resposta com status 500 e detalhes do erro
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> exception(Exception e){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Manipula exceções de login de usuário duplicado.
     *
     * @param e exceção lançada quando um login já existe no sistema
     * @return resposta com status 400 e detalhes do erro
     */
    @ExceptionHandler(UserLoginAlreadyTaken.class)
    private ResponseEntity<?> exception(UserLoginAlreadyTaken e){
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status_code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
