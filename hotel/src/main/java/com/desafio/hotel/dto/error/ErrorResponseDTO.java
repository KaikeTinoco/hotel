package com.desafio.hotel.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object para resposta de erros.
 *
 * <p>Padroniza o formato de retorno quando erros ocorrem na API.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Builder
@AllArgsConstructor
@Data
public class ErrorResponseDTO {
    /** Código HTTP do erro */
    private Integer status_code;

    /** Mensagem principal do erro */
    private String message;

    /** Lista de mensagens adicionais de erro (opcional) */
    private List<String> messages;
}
