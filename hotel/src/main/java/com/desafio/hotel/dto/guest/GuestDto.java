package com.desafio.hotel.dto.guest;

import lombok.Data;

/**
 * Data Transfer Object para dados de hóspede.
 *
 * <p>Contém as informações básicas de um hóspede para cadastro ou
 * recuperação de dados.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Data
public class GuestDto {
    /** Nome do hóspede */
    private String nome;

    /** Documento de identificação (CPF) do hóspede */
    private String documento;

    /** Telefone de contato do hóspede */
    private String telefone;

}
