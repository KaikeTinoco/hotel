package com.desafio.hotel.entity.checkout;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um check-out de um hóspede.
 *
 * <p>Armazena informações sobre a saída de um hóspede do hotel,
 * incluindo data de saída, valor total da estadia e referência ao check-in.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "Checkout")
@Table(name = "Checkouts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "Id")
public class Checkout {
    /** Identificador único do check-out */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    /** Check-in associado a este check-out (relação um-para-um) */
    @OneToOne
    @NotNull(message = "Um checkout precisa estar associado à um checkin!")
    @JoinColumn(name = "checkin_id", unique = true)
    private Checkin checkin;

    /** Valor total da estadia */
    @NotNull(message = "valorTotal não pode estar vazio!")
    private BigDecimal valorTotal;

    /** Data e hora de saída do hóspede */
    @NotNull(message = "por favor, informe a data de saída")
    private LocalDateTime dataSaida;


}
