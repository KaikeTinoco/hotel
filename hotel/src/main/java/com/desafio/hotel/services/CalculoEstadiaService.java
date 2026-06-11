package com.desafio.hotel.services;

import com.desafio.hotel.entity.checkout.Checkout;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface para o serviço de cálculo de estadias. Implementada por CalculoEstadiaServiceImpl.
 */
public interface CalculoEstadiaService {
    BigDecimal calcularValorEstadia(LocalDateTime dataEntrada, LocalDateTime dataSaida, boolean adicionalVeiculo);
    BigDecimal calcularTotalEstadias(Long clienteId, List<Checkout> checkoutList);
}

