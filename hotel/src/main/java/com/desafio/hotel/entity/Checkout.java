package com.desafio.hotel.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Checkout {

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    private Guest guest;

    @NotNull(message = "dataEntrada não pode estar vazio!")
    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    private boolean adicionalVeiculo;





}
