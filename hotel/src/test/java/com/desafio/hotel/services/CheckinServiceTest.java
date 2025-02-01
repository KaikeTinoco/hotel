package com.desafio.hotel.services;
import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.repositories.CheckinRepository;
import com.desafio.hotel.repositories.GuestRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
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
    private CheckinCreateDto checkinDto;
    private GuestDto dto;
    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        guest = criarHospede();
        checkin = criarCheckin();
        dto = criarDto();
        checkinDto = criarCheckinDto(guest);
    }

    @Test
    void criarCheckinTest() {
        Mockito.when(guestRepository.save(guest)).thenReturn(guest);
        Mockito.when(guestService.cadastrarHospede(dto)).thenReturn(guest);
        Mockito.when(checkinService.criarCheckin(checkinDto)).thenReturn(checkin);
        Mockito.when(checkinRepository.save(checkin)).thenReturn(checkin);
        Checkin checkinCreated = checkinService.criarCheckin(checkinDto);
        assertEquals(checkin, checkinCreated);
    }

    @Test
    void deletarCheckin() {
        Mockito.when(checkinRepository.findById(checkin.getId())).thenReturn(Optional.of(checkin));
        Mockito.when(checkinService.deletarCheckin(checkin.getId())).thenReturn("Checkin deletado com sucesso!");
        String message = checkinService.deletarCheckin(checkin.getId());
        assertEquals("Checkin deletado com sucesso!", message);
    }

    @Test
    void deletarCheckinNullId() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> checkinService.deletarCheckin(null));
        assertEquals("Por favor informe um id válido", exception.getMessage());
    }

    @Test
    void findByGuestId() {
        Mockito.when(guestRepository.save(guest)).thenReturn(guest);
        Mockito.when(guestService.findById(guest.getId())).thenReturn(guest);
        Mockito.when(checkinRepository.findByGuestId(guest.getId())).thenReturn(Optional.of(Arrays.asList(checkin)));
        List<Checkin> checkins = checkinService.findByGuestId(guest.getId());
        assertEquals(checkin, checkins.get(0));
    }

    @Test
    void findById() {
        Mockito.when(checkinRepository.findById(checkin.getId())).thenReturn(Optional.of(checkin));
        Checkin checkinFound = checkinService.findById(checkin.getId());
        assertEquals(checkin, checkinFound);
    }

    @Test
    void findByIdErrorIdNUll(){
        BadRequestException e = assertThrows(BadRequestException.class, () -> checkinService.findById(null));
        assertEquals("Por favor informe um id válido", e.getMessage());
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
    private Checkin criarCheckin(){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(criarHospede());
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