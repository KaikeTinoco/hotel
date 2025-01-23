package com.desafio.hotel.resource;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutControllerTest {
    @InjectMocks
    private CheckoutController checkoutController;

    @Mock
    private CheckoutService checkoutService;

    private Checkout checkout;

    private Guest guest;

    private Checkin checkin;

    private ResponseDTO dto;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        guest = criarHospede();
        checkout = criarCheckout(guest);
        checkin = criarCheckin(guest);
        dto = criarResponseDto(guest);
    }

    @Test
    void criarCheckout() {
        Checkout checkoutCreated = checkout;
        Checkin checkinCreated = checkin;
        Mockito.when(checkoutService.criarCheckout(checkinCreated.getId())).thenReturn(checkoutCreated);
        ResponseEntity response = checkoutController.criarCheckout(checkinCreated.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void buscarTodosHospedesDentroDoHotel() {
        ResponseDTO responseDTO = dto;
        List<ResponseDTO> dtos = Arrays.asList(responseDTO);
        Mockito.when(checkoutService.buscarTodosHospedesNoHotel()).thenReturn(dtos);
        ResponseEntity response = checkoutController.buscarTodosHospedesDentroDoHotel();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void buscarTodosHospedesForaDoHotel() {
        //a lógica é a mesma, não muda o que tem que fazer
        ResponseDTO responseDTO = dto;
        List<ResponseDTO> dtos = Arrays.asList(responseDTO);
        Mockito.when(checkoutService.buscarTodosHospedesForaHotel()).thenReturn(dtos);
        ResponseEntity response = checkoutController.buscarTodosHospedesDentroDoHotel();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private Checkout criarCheckout(Guest guest){
        Checkout checkout = new Checkout();
        checkout.setId(1L);
        checkout.setGuest(guest);
        checkout.setDataEntrada(LocalDateTime.of(2025,1,2,14,0));
        checkout.setDataSaida(LocalDateTime.now());
        checkout.setAdicionalVeiculo(true);
        checkout.setValorTotal(BigDecimal.valueOf(100));
        return checkout;
    }

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setId(1L);
        guest.setNome("Gustavo");
        guest.setDocumento("23583290");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }

    private Checkin criarCheckin(Guest guest){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(guest);
        checkin.setDataEntrada(LocalDateTime.now());
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }

    private ResponseDTO criarResponseDto(Guest guest){
        ResponseDTO dto = ResponseDTO.builder()
                .guest(guest)
                .totalHospedagens(BigDecimal.valueOf(100))
                .totalHospedagens(BigDecimal.valueOf(200))
                .build();
        return dto;
    }
}