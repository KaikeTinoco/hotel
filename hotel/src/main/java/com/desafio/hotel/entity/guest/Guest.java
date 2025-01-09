package com.desafio.hotel.entity.guest;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Guest")
@Table(name = "Guests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nome não pode estar em branco!")
    @NotNull(message = "nome não pode estar vazio!")
    private String nome;

    @NotBlank(message = "documento não pode estar em branco!")
    @NotNull(message = "documento não pode estar vazio!")
    private String documento;

    @NotBlank(message = "telefone não pode estar em branco!")
    @NotNull(message = "telefone não pode estar vazio!")
    private String telefone;
}
