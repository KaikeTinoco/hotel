package com.desafio.hotel.resource;

import com.desafio.hotel.dto.guest.GuestDto;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestControllerTest {
    @InjectMocks
    private GuestController guestController;

    @Mock
    private GuestService guestService;

    private Guest guest;

    private GuestDto dto;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        guest = criarHospede();
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

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setNome("Gustavo");
        guest.setDocumento("23583290");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }

    private GuestDto criarDto(){
        GuestDto dto = new GuestDto();
        dto.setNome("Gustavo");
        dto.setDocumento("23583290");
        dto.setTelefone("111222333444");
        return dto;
    }
}