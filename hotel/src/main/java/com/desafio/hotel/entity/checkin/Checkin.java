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


@Entity(name = "Checkin")
@Table(name = "Checkins")
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Checkin {

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    private Guest guest;

    @NotNull(message = "dataEntrada não pode estar vazio!")
    private LocalDateTime dataEntrada;

    private boolean adicionalVeiculo;

    private BigDecimal valorTotal;

    private LocalDateTime dataSaida;



}
