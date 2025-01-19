package com.desafio.hotel.resource;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.CheckinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CheckinControllerTest {

    @InjectMocks
    private CheckinController checkinController;

    @Mock
    private CheckinService checkinService;

    private Guest guest;

    private Checkin checkin;

    private CheckinCreateDto createDto;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        guest = criarHospede();
        checkin = criarCheckin(guest);
        createDto = criarCheckinDto(guest);
    }

    @Test
    void criarCheckinTest() {
        Checkin checkinCreated = checkin;
        CheckinCreateDto checkinDto = createDto;
        Mockito.when(checkinService.criarCheckin(checkinDto)).thenReturn(checkinCreated);
        ResponseEntity response = checkinController.criarCheckin(checkinDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deletarCheckinTest() {
        Checkin checkinCreated = checkin;
        Mockito.when(checkinService.deletarCheckin(checkinCreated.getId()))
                .thenReturn("Checkin deletado com sucesso!");
        String message = checkinService.deletarCheckin(checkinCreated.getId());
        assertEquals(message, "Checkin deletado com sucesso!");
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

    private CheckinCreateDto criarCheckinDto(Guest guest){
        CheckinCreateDto dto = new CheckinCreateDto();
        dto.setGuestId(guest.getId());
        dto.setAdicionalVeiculo(true);
        dto.setDataEntrada(LocalDateTime.now());
        return dto;
    }
}