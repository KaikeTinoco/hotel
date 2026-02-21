package com.desafio.hotel.services;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
class CalculoEstadiaServiceTest {

    /*Tabela de preços
       dia util sem carro = 120, com carro = 140
       fim de semana sem carro = 150, com carro = 170
    */
    private CalculoEstadiaService calculoEstadiaService;

    @BeforeEach
    void init(){
        calculoEstadiaService = new CalculoEstadiaService();
    }

    @Test
    void calcularValorEstadiaApenasDiasUteisSemCarro() {
        LocalDateTime entrada = LocalDateTime.of(2025,1, 20, 8, 30);
        LocalDateTime saida = LocalDateTime.of(2025,1,23,15,0);

        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(entrada,saida,false);

        assertEquals(BigDecimal.valueOf(360), valorTotal);
    }

    @Test
    void calcularValorEstadiaApenasDiasUteisComCarro() {
        LocalDateTime entrada = LocalDateTime.of(2025,1, 20, 8, 30);
        LocalDateTime saida = LocalDateTime.of(2025,1,23,15,0);

        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(entrada,saida,true);

        assertEquals(BigDecimal.valueOf(420), valorTotal);
    }


    @Test
    void calcularValorEstadiaDiasUteisComFimDeSemanaSemCarro() {
        LocalDateTime entrada = LocalDateTime.of(2025,1, 18, 8, 30);
        LocalDateTime saida = LocalDateTime.of(2025,1,23,15,0);

        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(entrada,saida,false);

        assertEquals(BigDecimal.valueOf(660), valorTotal);
    }

    @Test
    void calcularValorEstadiaDiasUteisComFimDeSemanaComCarro() {
        LocalDateTime entrada = LocalDateTime.of(2025,1, 18, 8, 30);
        LocalDateTime saida = LocalDateTime.of(2025,1,23,15,0);

        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(entrada,saida,true);

        assertEquals(BigDecimal.valueOf(760), valorTotal);
    }

    @Test
    void calcularValorEstadiaComAdicionalDeSaida() {
        LocalDateTime entrada = LocalDateTime.of(2025,1, 20, 8, 30);
        LocalDateTime saida = LocalDateTime.of(2025,1,23,17,0);
        //tem q cobrar mais uma diaria por sair apos as 16:30

        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(entrada,saida,false);

        assertEquals(BigDecimal.valueOf(480), valorTotal);
    }

    @Test
    void retornaZeroSeHospedeNaoTiverCheckoutsAnteriores(){
        BigDecimal total = calculoEstadiaService.calcularTotalEstadias(1L, List.of());

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void ignoraOsCheckoutsDuplicadosNoCalculoTotalGasto(){
        Checkout checkout = criarCheckout();
        checkout.setId(1L);
        checkout.setValorTotal(BigDecimal.valueOf(100));
        Checkout checkoutDois = criarCheckout();
        checkoutDois.setId(2L);
        checkoutDois.setValorTotal(BigDecimal.valueOf(200));
        Checkout checkoutTres = checkoutDois;


        BigDecimal total = calculoEstadiaService.calcularTotalEstadias(1L, List.of(checkout,
                checkoutDois, checkoutTres));

        assertEquals(BigDecimal.valueOf(300), total);
    }

    @Test
    void lancaErroSeDataEntradaForMaiorQueDataSaida(){
        LocalDateTime saida = LocalDateTime.of(2025,1, 20, 8, 30);
        LocalDateTime entrada  = LocalDateTime.of(2025,1,23,17,0);

        assertThrows(BadRequestException.class, () -> calculoEstadiaService.calcularValorEstadia(entrada,
                saida, false));
    }

    @Test
    void calcularValorEstadiaEntradaSaidaMesmoDia() {
        LocalDateTime entrada = LocalDateTime.of(2025,1,20,8,30);
        LocalDateTime saida = LocalDateTime.of(2025,1,20,20,0);

        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(entrada, saida, false);

        assertEquals(BigDecimal.valueOf(120), valorTotal);
    }

    private Checkout criarCheckout(){
        Guest guest = new Guest(1L, "Pedro", "123", "456", true);
        Checkout checkout = new Checkout();
        checkout.setCheckin(criarCheckin());
        checkout.getCheckin().setGuest(guest);
        checkout.getCheckin().setAdicionalVeiculo(false);
        checkout.getCheckin().setDataEntrada(LocalDateTime.of(2025,1, 20, 8, 30));
        checkout.setDataSaida(LocalDateTime.of(2025,1,23,17,0));
        return checkout;
    }

    private Checkin criarCheckin(){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(criarHospede());
        checkin.setDataEntrada(LocalDateTime.now());
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setId(1L);
        guest.setNome("Gustavo");
        guest.setDocumento("183.079.440-07");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }
}