package com.desafio.hotel.services;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.CheckinRepository;
import com.desafio.hotel.repositories.GuestRepository;
import org.hibernate.annotations.Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CheckinServiceTest {
    @InjectMocks
    private CheckinService checkinService;

    @Mock
    private CheckinRepository checkinRepository;

    @Mock
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    private Guest guest;

    private Checkin checkin;

    private GuestDto dto;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        guest = criarHospede();
        checkin = criarCheckin(guest);
        dto = criarDto();
    }

    @Test
    void criarCheckinTest() {
        GuestDto guestDto = criarDto();
        Mockito.when(guestService.cadastrarHospede(guestDto)).thenReturn(guest);
        Mockito.when(guestRepository.save(guest)).thenReturn(guest);

        Guest guestFound = guestService.cadastrarHospede(guestDto);
        CheckinCreateDto createDto = criarCheckinDto(guestFound);
        Checkin checkinCreate = criarCheckin(guestFound);
        Mockito.when(checkinService.criarCheckin(createDto)).thenReturn(checkinCreate);
        Mockito.when(checkinRepository.save(checkinCreate)).thenReturn(checkinCreate);
        Mockito.when(guestService.findById(createDto.getGuestId())).thenReturn(guestFound);
        Mockito.when(guestRepository.findById(createDto.getGuestId())).thenReturn(Optional.of(guestFound));
        Checkin checkinFound = checkinService.criarCheckin(createDto);

        assertNotNull(checkinFound);
        assertEquals("Gustavo", checkinFound.getGuest().getNome());
        assertTrue(checkinFound.getGuest().isDentroHotel());
    }


    @Test
    void deletarCheckin() {
        Mockito.when(checkinRepository.save(criarCheckin(guest))).thenReturn(checkin);
        Mockito.when(checkinRepository.findById(checkin.getId())).thenReturn(Optional.of(checkin));


        String message = checkinService.deletarCheckin(checkin.getId());
        assertEquals("Checkin deletado com sucesso!", message);
    }

    @Test
    void findByGuestId() {
        Guest guestCreated = criarHospede();

        Mockito.when(guestService.cadastrarHospede((dto))).thenReturn(guest);
        Mockito.when(checkinService.criarCheckin(criarCheckinDto(guestCreated))).thenReturn(checkin);
        Checkin checkinCreated = checkinService.criarCheckin(criarCheckinDto(guestCreated));
        List<Checkin> checkins = Arrays.asList(checkinCreated);

        Mockito.when(checkinService.findByGuestId(checkinCreated.getGuest().getId())).thenReturn(checkins);
        List<Checkin> checkinsFound = checkinService.findByGuestId(checkinCreated.getGuest().getId());
        assertEquals(checkinsFound.size(), checkins.size());
        assertEquals(checkinsFound.get(0), checkins.get(0));
    }

    @Test
    void findById() {
        Guest guestB = criarHospede();
        Mockito.when(guestService.cadastrarHospede((dto))).thenReturn(guest);
        Mockito.when(checkinService.criarCheckin(criarCheckinDto(guestB))).thenReturn(checkin);
        Checkin checkinB = checkinService.criarCheckin(criarCheckinDto(guestB));
        Mockito.when(checkinService.findById(checkinB.getId())).thenReturn(checkin);
        Checkin checkinA = checkinService.findById(checkinB.getId());
        assertEquals(checkinA, checkinB);
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

    private GuestDto criarDto(){
        GuestDto dto = new GuestDto();
        dto.setNome("Gustavo");
        dto.setDocumento("23583290");
        dto.setTelefone("111222333444");
        return dto;
    }

    private CheckinCreateDto criarCheckinDto(Guest guest){
        CheckinCreateDto dto = new CheckinCreateDto();
        dto.setGuestId(guest.getId());
        dto.setAdicionalVeiculo(true);
        dto.setDataEntrada(LocalDateTime.now());
        return dto;
    }
}