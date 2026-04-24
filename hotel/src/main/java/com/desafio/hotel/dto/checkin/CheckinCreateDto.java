package com.desafio.hotel.dto.checkin;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object para criação de check-in.
 *
 * <p>Contém os dados necessários para registrar a entrada de um hóspede no hotel.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckinCreateDto {
    /** Identificador único do hóspede */
    private Long guestId;

    /** Data e hora de entrada do hóspede */
    private LocalDateTime dataEntrada;

    /** Indica se o hóspede possui veículo estacionado no hotel */
    private boolean adicionalVeiculo;
}
