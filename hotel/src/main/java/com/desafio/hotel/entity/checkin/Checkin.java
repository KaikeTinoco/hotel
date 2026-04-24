package com.desafio.hotel.entity.checkin;

import com.desafio.hotel.entity.guest.Guest;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um check-in de um hóspede.
 *
 * <p>Armazena informações sobre a entrada de um hóspede no hotel,
 * incluindo data de entrada e informações sobre veículo.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "Checkin")
@Table(name = "Checkins")
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "Id")
public class Checkin {

    /** Identificador único do check-in */
    @Id
    @GeneratedValue
    private Long Id;

    /** Hóspede associado a este check-in */
    @ManyToOne
    private Guest guest;

    /** Data e hora de entrada do hóspede */
    @NotNull(message = "dataEntrada não pode estar vazio!")
    private LocalDateTime dataEntrada;

    /** Indica se o hóspede possui veículo estacionado no hotel */
    @NotNull(message = "adicionalVeiculo não pode estar vazio!")
    private boolean adicionalVeiculo;


}
