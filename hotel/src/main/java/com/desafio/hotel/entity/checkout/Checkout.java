package com.desafio.hotel.entity.checkout;

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

@Entity(name = "Checkout")
@Table(name = "Checkouts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "Id")
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    private Guest guest;

    @NotNull(message = "dataEntrada não pode estar vazio!")
    private LocalDateTime dataEntrada;

    @NotNull(message = "dataSaida não pode estar vazio!")
    private LocalDateTime dataSaida;

    @NotNull(message = "adicionalVeiculo não pode estar vazio!")
    private boolean adicionalVeiculo;

    @NotNull(message = "valorTotal não pode estar vazio!")
    private BigDecimal valorTotal;


}
