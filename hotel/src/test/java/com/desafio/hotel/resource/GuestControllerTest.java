package com.desafio.hotel.resource;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class GuestControllerTest {
    @InjectMocks
    private GuestController guestController;

    @Mock
    private GuestService guestService;

    private Guest guest;

    private GuestDto dto;

    private Checkin checkin;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        guest = criarHospede();
        checkin = criarCheckin(guest);
        dto = criarDto();
    }

    @Test
    void cadastrarHospede() {
        Guest guestCadastrado = guest;
        guestCadastrado.setId(1L);
        Mockito.when(guestService.cadastrarHospede(dto)).thenReturn(guestCadastrado);
        ResponseEntity response = guestController.cadastrarHospede(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void buscarTodosHospedes() {
        List<Guest> guests = Arrays.asList(guest);
        Mockito.when(guestService.buscarTodosHospedes()).thenReturn(guests);
        ResponseEntity response = guestController.buscarTodosHospedes();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void deletarHospede() {
        Guest guestDeletado = guest;
        guestDeletado.setId(1L);
        Mockito.when(guestService.cadastrarHospede(dto)).thenReturn(guestDeletado);
        Mockito.when(guestService.findById(guestDeletado.getId())).thenReturn(guestDeletado);

        ResponseEntity response = guestController.deletarHospede(guestDeletado.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Checkout criarCheckout(){
        Guest guest = new Guest(1L, "Pedro", "000.240.470-23", "6724581391", true);
        Checkout checkout = new Checkout();
        checkout.setCheckin(criarCheckin(guest));
        checkout.getCheckin().setGuest(guest);
        checkout.getCheckin().setAdicionalVeiculo(false);
        checkout.getCheckin().setDataEntrada(LocalDateTime.of(2025,1, 20, 8, 30));
        checkout.setDataSaida(LocalDateTime.of(2025,1,23,17,0));
        return checkout;
    }

    private Checkin criarCheckin(Guest guest){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(guest);
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

    private GuestDto criarDto(){
        GuestDto dto = new GuestDto();
        dto.setNome("gustavo");
        dto.setDocumento("183.079.440-07");
        dto.setTelefone("111222333444");
        return dto;
    }
}