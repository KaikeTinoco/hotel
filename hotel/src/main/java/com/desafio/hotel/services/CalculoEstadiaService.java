package com.desafio.hotel.services;

import com.desafio.hotel.entity.checkout.Checkout;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CalculoEstadiaService {

    private static final BigDecimal VALOR_UTIL_SEM_CARRO = new BigDecimal("120");
    private static final BigDecimal VALOR_UTIL_COM_CARRO = new BigDecimal("140");
    private static final BigDecimal VALOR_FINAL_SEMANA_SEM_CARRO = new BigDecimal("150");
    private static final BigDecimal VALOR_FINAL_SEMANA_COM_CARRO = new BigDecimal("170");

    public BigDecimal calcularValorEstadia(LocalDateTime dataEntrada,
                                           LocalDateTime dataSaida,
                                           boolean adicionalVeiculo) {
        long totalDias = ChronoUnit.DAYS.between(dataEntrada.toLocalDate(), dataSaida.toLocalDate());
        long finaisDeSemana = contarFinaisDeSemana(dataEntrada, dataSaida);
        long diasUteis = totalDias - finaisDeSemana;

        BigDecimal valorDiaUtil = adicionalVeiculo ? VALOR_UTIL_COM_CARRO : VALOR_UTIL_SEM_CARRO;
        BigDecimal valorFinalDeSemana = adicionalVeiculo ? VALOR_FINAL_SEMANA_COM_CARRO : VALOR_FINAL_SEMANA_SEM_CARRO;

        BigDecimal valorTotal = valorDiaUtil.multiply(BigDecimal.valueOf(diasUteis))
                .add(valorFinalDeSemana.multiply(BigDecimal.valueOf(finaisDeSemana)));

        if (checarHoraSaida(dataSaida)) {
            if (dataSaida.getDayOfWeek() == DayOfWeek.SATURDAY || dataSaida.getDayOfWeek() == DayOfWeek.SUNDAY) {
                valorTotal = valorTotal.add(valorFinalDeSemana);
            } else {
                valorTotal = valorTotal.add(valorDiaUtil);
            }
        }

        return valorTotal;
    }

    private long contarFinaisDeSemana(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        long finaisDeSemana = 0;
        for (LocalDateTime data = dataEntrada; !data.toLocalDate().isAfter(dataSaida.toLocalDate());
             data = data.plusDays(1)) {
            if (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
                finaisDeSemana++;
            }
        }
        return finaisDeSemana;
    }

    private boolean checarHoraSaida(LocalDateTime dataSaida) {
        int hora = dataSaida.getHour();
        int minutos = dataSaida.getMinute();
        return hora >= 16 && minutos >= 30;
    }

    // Map para armazenar o total acumulado por cliente
    private final Map<Long, BigDecimal> totalPorCliente = new HashMap<>();

    // Map para armazenar os IDs de checkouts processados por cliente
    private final Map<Long, Set<Long>> checkoutsProcessadosPorCliente = new HashMap<>();

    public BigDecimal calcularTotalEstadias(Long clienteId, List<Checkout> checkoutList) {
        // Inicializa o Set de checkouts processados para o cliente, se necessário
        checkoutsProcessadosPorCliente.putIfAbsent(clienteId, new HashSet<>());
        Set<Long> checkoutsProcessados = checkoutsProcessadosPorCliente.get(clienteId);

        // Obtém o total acumulado atual ou inicializa com ZERO
        BigDecimal totalAtual = totalPorCliente.getOrDefault(clienteId, BigDecimal.ZERO);

        for (Checkout checkout : checkoutList) {
            // Soma apenas se o checkout ainda não foi processado
            if (!checkoutsProcessados.contains(checkout.getId())) {
                totalAtual = totalAtual.add(checkout.getValorTotal());
                checkoutsProcessados.add(checkout.getId()); // Marca como processado
            }
        }

        // Atualiza o total acumulado para o cliente
        totalPorCliente.put(clienteId, totalAtual);
        return totalAtual;
    }
}

