package com.desafio.hotel.entity.guest;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

/**
 * Entidade que representa um hóspede do hotel.
 *
 * <p>Armazena informações pessoais e dados de hospedagem de um hóspede,
 * incluindo nome, documento, telefone e status de permanência no hotel.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "Guest")
@Table(name = "Guests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Guest {
    /** Identificador único do hóspede */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do hóspede */
    @NotBlank(message = "nome não pode estar em branco!")
    @NotNull(message = "nome não pode estar vazio!")
    private String nome;

    /** Número de CPF do hóspede (validado conforme padrão brasileiro) */
    @NotBlank(message = "documento não pode estar em branco!")
    @NotNull(message = "documento não pode estar vazio!")
    @CPF
    private String documento;

    /** Telefone de contato do hóspede */
    @NotBlank(message = "telefone não pode estar em branco!")
    @NotNull(message = "telefone não pode estar vazio!")
    private String telefone;

    /** Indica se o hóspede está atualmente dentro do hotel */
    @NotNull(message = "dentroHotel não pode estar vazio!")
    private boolean dentroHotel;
}
