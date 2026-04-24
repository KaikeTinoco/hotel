package com.desafio.hotel.services;

import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Serviço responsável pelo cálculo de estadias em hotel.
 *
 * <p>Esta classe fornece funcionalidades para calcular o valor total de uma estadia
 * baseado em datas de entrada e saída, considerando tarifa diferenciada para finais
 * de semana e a possibilidade de adicional por veículo.</p>
 *
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Cálculo de valor de estadia com base em data de entrada e saída</li>
 *   <li>Consideração de tarifas diferenciadas para dias úteis e finais de semana</li>
 *   <li>Adicional de tarifa para hóspedes com veículo</li>
 *   <li>Cálculo do total de despesas por cliente ao longo de múltiplas estadias</li>
 * </ul>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Service
public class CalculoEstadiaService {

    /** Valor da diária em dia útil sem veículo */
    private static final BigDecimal VALOR_UTIL_SEM_CARRO = new BigDecimal("120");

    /** Valor da diária em dia útil com veículo */
    private static final BigDecimal VALOR_UTIL_COM_CARRO = new BigDecimal("140");

    /** Valor da diária em final de semana sem veículo */
    private static final BigDecimal VALOR_FINAL_SEMANA_SEM_CARRO = new BigDecimal("150");

    /** Valor da diária em final de semana com veículo */
    private static final BigDecimal VALOR_FINAL_SEMANA_COM_CARRO = new BigDecimal("170");

    /**
     * Calcula o valor total da estadia de um hóspede.
     *
     * <p>O cálculo considera:</p>
     * <ul>
     *   <li>Tarifa diferenciada para dias úteis e finais de semana</li>
     *   <li>Tarifa adicional se o hóspede tiver veículo</li>
     *   <li>Cobrança adicional se a saída ocorrer após as 16h30</li>
     * </ul>
     *
     * @param dataEntrada data e hora de entrada do hóspede
     * @param dataSaida data e hora de saída do hóspede
     * @param adicionalVeiculo indica se o hóspede possui veículo
     * @return o valor total da estadia em BigDecimal
     * @throws BadRequestException se a data de saída for anterior à data de entrada
     */
    public BigDecimal calcularValorEstadia(LocalDateTime dataEntrada,
                                           LocalDateTime dataSaida,
                                           boolean adicionalVeiculo) {
        if (dataSaida.isBefore(dataEntrada)){
            throw new BadRequestException("A data de entrada não pode ser depois da data de saída!");
        }

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

    /**
     * Conta o número de finais de semana entre duas datas.
     *
     * @param dataEntrada data de início da contagem
     * @param dataSaida data de fim da contagem
     * @return número total de dias que caem em finais de semana
     */
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

    /**
     * Verifica se o horário de saída ultrapassa as 16h30.
     *
     * <p>Se a saída ocorrer após este horário, uma diária adicional é cobrada.</p>
     *
     * @param dataSaida data e hora de saída a ser verificada
     * @return true se a hora é posterior às 16h30, false caso contrário
     */
    private boolean checarHoraSaida(LocalDateTime dataSaida) {
        if(dataSaida.getHour() > 16){
            return true;
        } else return dataSaida.getHour() == 16 && dataSaida.getMinute() == 30;
    }

    /** Mapa que armazena o total de despesas por cliente */
    private final Map<Long, BigDecimal> totalPorCliente = new HashMap<>();

    /** Mapa que rastreia quais checkouts já foram processados para cada cliente */
    private final Map<Long, Set<Long>> checkoutsProcessadosPorCliente = new HashMap<>();

    /**
     * Calcula o total de despesas de um cliente ao longo de múltiplas estadias.
     *
     * <p>Este método evita contar a mesma estadia duas vezes, rastreando os IDs
     * dos checkouts já processados para cada cliente.</p>
     *
     * @param clienteId identificador único do cliente
     * @param checkoutList lista de checkouts do cliente
     * @return o valor total acumulado das estadias do cliente
     */
    public BigDecimal calcularTotalEstadias(Long clienteId, List<Checkout> checkoutList) {
        checkoutsProcessadosPorCliente.putIfAbsent(clienteId, new HashSet<>());
        Set<Long> checkoutsProcessados = checkoutsProcessadosPorCliente.get(clienteId);

        BigDecimal totalAtual = totalPorCliente.getOrDefault(clienteId, BigDecimal.ZERO);

        for (Checkout checkout : checkoutList) {
            if (!checkoutsProcessados.contains(checkout.getId())) {
                totalAtual = totalAtual.add(checkout.getValorTotal());
                checkoutsProcessados.add(checkout.getId());
            }
        }

        totalPorCliente.put(clienteId, totalAtual);
        return totalAtual;
    }
}
