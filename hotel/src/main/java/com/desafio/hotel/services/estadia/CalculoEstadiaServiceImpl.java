package com.desafio.hotel.services.estadia;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.services.checkin.CheckinServiceImpl;
import com.desafio.hotel.services.checkout.CheckoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CalculoEstadiaServiceImpl implements CalculoEstadiaService {


    private final CheckoutServiceImpl checkoutService;
    private final CheckinServiceImpl checkinService;
    @Autowired
    public CalculoEstadiaServiceImpl(CheckoutServiceImpl checkoutService, CheckinServiceImpl checkinService) {
        this.checkoutService = checkoutService;
        this.checkinService = checkinService;
    }


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
        return dataSaida.isAfter(dataSaida.toLocalDate().atTime(16, 30));
    }


    /**
     * Calcula o total das estadias de um cliente.
     *
     * <p>Esta função soma o valor total de todas as estadias realizadas por um determinado cliente,
     * utilizando a lista de checkouts associados ao ID do cliente.</p>
     *
     * @param checkinId ID do checkin do cliente
     * @return o valor total das estadias em BigDecimal
     */
    @Override
    public BigDecimal calcularValorEstadia(Long checkinId) {
        Checkin checkin = checkinService.findById(checkinId);
        LocalDateTime dataEntrada = checkin.getDataEntrada();
        LocalDateTime dataSaida = LocalDateTime.now();
        boolean adicionalVeiculo = checkin.isAdicionalVeiculo();
        long diasTotais = ChronoUnit.DAYS.between(dataEntrada.toLocalDate(), dataSaida.toLocalDate()) + 1;
        long finaisDeSemana = contarFinaisDeSemana(dataEntrada, dataSaida);
        long diasUteis = diasTotais - finaisDeSemana;
        boolean diariaAdicional = checarHoraSaida(dataSaida);


        BigDecimal valorUtil, valorFimDeSemana;
        BigDecimal total = new BigDecimal(0);

        if(adicionalVeiculo){
             valorUtil = BigDecimal.valueOf(140);
             valorFimDeSemana = BigDecimal.valueOf(170);
        } else {
             valorUtil = BigDecimal.valueOf(120);
             valorFimDeSemana = BigDecimal.valueOf(150);
        }
        total.add(BigDecimal.valueOf(diasUteis).multiply(valorUtil));
        total.add(BigDecimal.valueOf(finaisDeSemana).multiply(valorFimDeSemana));
        if (diariaAdicional) {
            total.add(BigDecimal.valueOf(100));
        }
        return total;
    }

    @Override
    public BigDecimal calcularTotalEstadias(Long clienteId) {
        List<Checkout> checkouts = checkoutService.listarTodosCheckoutsDoCliente(clienteId);
        return checkouts.stream()
                .map(Checkout::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}




