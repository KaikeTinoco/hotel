package com.desafio.hotel.dto.response;

import com.desafio.hotel.entity.guest.Guest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object para resposta com informações de hóspede e custos.
 *
 * <p>Retorna informações completas de um hóspede incluindo o valor total
 * gasto em todas as suas hospedagens e o valor da última hospedagem.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseDTO {
    /** Objeto contendo os dados do hóspede */
    private Guest guest;

    /** Valor total acumulado de todas as hospedagens do hóspede */
    private BigDecimal totalHospedagens;

    /** Valor da última hospedagem realizada pelo hóspede */
    private BigDecimal totalUltimaHospedagem;
}
