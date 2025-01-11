package com.desafio.hotel.dto.response;

import com.desafio.hotel.entity.guest.Guest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseDTO {
    private Guest guest;
    private BigDecimal totalHospedagens;
    private BigDecimal totalUltimaHospedagem;
}
