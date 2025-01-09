package com.desafio.hotel.dto.checkin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckinCreateDto {
    private Long guestId;
    private LocalDateTime dataEntrada;
    private boolean adicionalVeiculo;
}
