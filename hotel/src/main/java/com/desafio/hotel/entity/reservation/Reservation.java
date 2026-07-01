package com.desafio.hotel.entity.reservation;

import com.desafio.hotel.entity.guest.Guest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @NotNull
    private LocalDateTime dataPrevistaEntrada;

    @NotNull
    private LocalDateTime dataPrevistaSaida;

    @NotNull
    private int quantidadePessoas;



    private LocalDateTime dataCriacao;


}
