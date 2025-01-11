package com.desafio.hotel.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class ErrorResponseDTO {
    private Integer status_code;
    private String message;
    private List<String> messages;
}
