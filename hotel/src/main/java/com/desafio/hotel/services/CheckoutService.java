package com.desafio.hotel.services;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.repositories.CheckoutRepository;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private CheckoutRepository repository;

    @Autowired
    private CheckinService checkinService; // Interface injection

    @Autowired
    private CalculoEstadiaService calculoEstadiaService;



    public Checkout criarCheckout(Long id) throws Exception {
        Checkin checkin = checkinService.findById(id);
        Checkout checkout = new Checkout();
        checkout.setGuest(checkin.getGuest());
        checkout.setDataEntrada(checkin.getDataEntrada());
        checkout.setDataSaida(LocalDateTime.now());
        checkout.setAdicionalVeiculo(checkin.isAdicionalVeiculo());
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(checkin.getDataEntrada(),
                LocalDateTime.now(), checkin.isAdicionalVeiculo());
        checkout.setValorTotal(valorTotal);
        repository.save(checkout);
        return checkout;
    }

    public List<Checkout> findByGuestId(Long id) throws Exception {
        return repository.findByGuestId(id)
                .orElseThrow(()-> new Exception("não há nenhum checkout associado a esse cliente"));
    }


}
