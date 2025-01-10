package com.desafio.hotel.services;

import com.desafio.hotel.entity.checkout.Checkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalculoEstadiaService {

    public BigDecimal calcularTotalEstadias(List<Checkout> checkoutList){
        BigDecimal totalEstadias = new BigDecimal(0);
        for(Checkout checkout: checkoutList){
            totalEstadias = totalEstadias.add(
                    calcularValorEstadia(
                            checkout.getDataEntrada(),
                            checkout.getDataSaida(),
                            checkout.isAdicionalVeiculo()));
        }
        return totalEstadias;
    }

    public BigDecimal calcularValorEstadia(LocalDateTime dataEntrada,
                                            LocalDateTime dataSaida,
                                            boolean adicionalVeiculo){
        BigDecimal valorTotal = new BigDecimal(0);

        if(adicionalVeiculo){
            while(!(dataEntrada.isEqual(dataSaida))){
                if(dataEntrada.getDayOfWeek() == DayOfWeek.SUNDAY
                        || dataEntrada.getDayOfWeek() == DayOfWeek.SATURDAY){
                    valorTotal = valorTotal.add(new BigDecimal("170"));
                } else {
                    valorTotal = valorTotal.add(new BigDecimal("140"));
                }
                dataEntrada = dataEntrada.plusDays(1);
            }
            if (checarHoraSaida(dataSaida)){
                if(dataEntrada.getDayOfWeek() == DayOfWeek.SUNDAY
                        || dataEntrada.getDayOfWeek() == DayOfWeek.SATURDAY){
                    valorTotal = valorTotal.add(new BigDecimal("170"));
                } else {
                    valorTotal = valorTotal.add(new BigDecimal("140"));
                }
            }
        } else{
            while(!(dataEntrada.isEqual(dataSaida))){
                if(dataEntrada.getDayOfWeek() == DayOfWeek.SUNDAY
                        || dataEntrada.getDayOfWeek() == DayOfWeek.SATURDAY){
                    valorTotal = valorTotal.add(new BigDecimal("150"));
                } else {
                    valorTotal = valorTotal.add(new BigDecimal("120"));
                }
                dataEntrada = dataEntrada.plusDays(1);
            }
            if (checarHoraSaida(dataSaida)){
                if(dataEntrada.getDayOfWeek() == DayOfWeek.SUNDAY
                        || dataEntrada.getDayOfWeek() == DayOfWeek.SATURDAY){
                    valorTotal = valorTotal.add(new BigDecimal("150"));
                } else {
                    valorTotal = valorTotal.add(new BigDecimal("120"));
                }
            }
        }
        return valorTotal;

    }

    private boolean checarHoraSaida(LocalDateTime dataSaida){
        int hora = dataSaida.getHour();
        int minutos = dataSaida.getMinute();

        if (hora >= 16 && minutos >= 30){
            return true;
        } else {
            return false;
        }
    }


}
