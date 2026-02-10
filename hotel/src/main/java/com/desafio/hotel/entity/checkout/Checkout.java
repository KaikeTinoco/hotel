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

    @OneToOne
    @NotNull(message = "Um checkout precisa estar associado à um checkin!")
    @JoinColumn(name = "checkin_id", unique = true)
    private Checkin checkin;

    @NotNull(message = "valorTotal não pode estar vazio!")
    private BigDecimal valorTotal;

    @NotNull(message = "por favor, informe a data de saída")
    private LocalDateTime dataSaida;


}
