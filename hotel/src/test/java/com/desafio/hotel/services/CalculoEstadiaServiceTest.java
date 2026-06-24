package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.repositories.CheckoutRepository;
import com.desafio.hotel.services.estadia.CalculoEstadiaServiceImpl;
import com.desafio.hotel.services.checkin.CheckinServiceImpl;
import com.desafio.hotel.services.checkout.CheckoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class CalculoEstadiaServiceTest {

    /*Tabela de preços
       dia util sem carro = 120, com carro = 140
       fim de semana sem carro = 150, com carro = 170
    */
    @InjectMocks
    private CalculoEstadiaServiceImpl calculoEstadiaService;

    @Mock
    private CheckoutServiceImpl checkoutService;

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private CheckinServiceImpl checkinService;

    private Guest hospede;

    private Checkout checkout;

    private GuestDto guestDto;

    private Checkin checkin;

    private ResponseDTO responseDTO;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        hospede = criarHospede();
        checkin = criarCheckin(hospede);
        checkout = criarCheckout(checkin);
    }

    @Test
    void calcularValorEstadiaApenasDiasUteisSemCarro() {
        Checkin c = checkin;
        c.setAdicionalVeiculo(false);
        Mockito.when(checkinService.findById(c.getId())).thenReturn(c);
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(c.getId());

        assertEquals(BigDecimal.valueOf(220), valorTotal);
    }

    @Test
    void calcularValorEstadiaApenasDiasUteisComCarro() {

        Mockito.when(checkinService.findById(checkin.getId())).thenReturn(checkin);
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(checkin.getId());

        assertEquals(BigDecimal.valueOf(240), valorTotal);
    }


    @Test
    void calcularValorEstadiaDiasUteisComFimDeSemanaSemCarro() {
        Checkin c = checkin;
        c.setAdicionalVeiculo(false);
        c.setDataEntrada(LocalDateTime.now().minusDays(5));

        Mockito.when(checkinService.findById(c.getId())).thenReturn(c);
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(c.getId());

        assertEquals(BigDecimal.valueOf(760), valorTotal);
    }

    @Test
    void calcularValorEstadiaDiasUteisComFimDeSemanaComCarro() {
        Checkin c = checkin;
        c.setAdicionalVeiculo(true);
        c.setDataEntrada(LocalDateTime.now().minusDays(5));

        Mockito.when(checkinService.findById(c.getId())).thenReturn(c);
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(c.getId());

        assertEquals(BigDecimal.valueOf(860), valorTotal);
    }

    @Test
    void calcularValorEstadiaComAdicionalDeSaida() {
        Mockito.when(checkinService.findById(checkin.getId())).thenReturn(checkin);
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(checkin.getId());

        assertEquals(BigDecimal.valueOf(240), valorTotal);
    }

    @Test
    void retornaZeroSeHospedeNaoTiverCheckoutsAnteriores(){
        Mockito.when(checkoutService.listarTodosCheckoutsDoCliente(1L)).thenReturn(Arrays.asList());
        List<Checkout> checkouts = checkoutService.listarTodosCheckoutsDoCliente(1L);
        BigDecimal total = calculoEstadiaService.calcularTotalEstadias(1L,checkouts);

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void ignoraOsCheckoutsDuplicadosNoCalculoTotalGasto(){
        Checkout checkout = criarCheckout(checkin);
        checkout.setId(1L);
        checkout.setValorTotal(BigDecimal.valueOf(100));
        Checkout checkoutDois = criarCheckout(checkin);
        checkoutDois.setId(2L);
        checkoutDois.setValorTotal(BigDecimal.valueOf(200));
        Checkout checkoutTres = checkoutDois;

        Mockito.when(checkoutRepository.findByCheckinGuestId(1L)).thenReturn(Arrays.asList(checkout, checkoutDois, checkoutTres));
        when(checkoutService.listarTodosCheckoutsDoCliente(1L))
                .thenReturn(Arrays.asList(checkout, checkoutDois, checkoutTres));
        List<Checkout> checkouts = checkoutService.findByCheckinId(1L);
        BigDecimal total = calculoEstadiaService.calcularTotalEstadias(1L, checkouts);

        assertEquals(BigDecimal.valueOf(500), total);
    }

    @Test
    void lancaErroSeDataEntradaForMaiorQueDataSaida(){
        Mockito.when(checkinService.findById(checkin.getId())).thenThrow(new BadRequestException(""));

        assertThrows(BadRequestException.class, () -> calculoEstadiaService.calcularValorEstadia(checkin.getId()));
    }

    @Test
    void calcularValorEstadiaEntradaSaidaMesmoDia() {
        Checkin c = checkin;
        c.setDataEntrada(LocalDateTime.now().minusHours(2));
        Mockito.when(checkinService.findById(checkin.getId())).thenReturn(checkin);
        BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(checkin.getId());


        assertEquals(BigDecimal.valueOf(100), valorTotal);
    }

    private Checkout criarCheckout(Checkin checkin){
        Checkout checkout = new Checkout();
        checkout.setCheckin(checkin);
        checkout.setDataSaida(LocalDateTime.now());
        return checkout;
    }

    private Checkin criarCheckin(Guest hospede){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(hospede);
        checkin.setDataEntrada(LocalDateTime.now().minusDays(1));
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