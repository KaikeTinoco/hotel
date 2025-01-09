package com.desafio.hotel.dto.checkin;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckinCreateDto {
    private Long guestId;
    private LocalDateTime dataEntrada;
    private boolean adicionalVeiculo;
}
