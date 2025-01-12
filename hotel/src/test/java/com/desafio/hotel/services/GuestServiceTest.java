package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
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
        Mockito.when(this.guestRepository.save(guest)).thenReturn(guest);
        Guest hospedeSalvo = new Guest();
        hospedeSalvo = guestService.cadastrarHospede(dto);

        assertEquals(hospedeSalvo.getNome(), "Gustavo");
        assertEquals(hospedeSalvo.getDocumento(),"23583290");
        assertEquals(hospedeSalvo.getTelefone(),"111222333444");
        assertEquals(hospedeSalvo.isDentroHotel(), true);
    }

    @Test
    void deletarGuestById() {
        guest.setId(1L);
        Mockito.when(this.guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        String mensagem = guestService.deletarGuestById(guest.getId());
        assertEquals(mensagem,"Hóspede deletado com sucesso!" );
    }

    @Test
    void findById() {
        guest.setId(1L);
        Mockito.when(this.guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        Guest guestFound = guestService.findById(1L);
        assertEquals(guestFound.getNome(), "Gustavo");
        assertEquals(guestFound.getDocumento(),"23583290");
        assertEquals(guestFound.getTelefone(),"111222333444");
        assertEquals(guestFound.isDentroHotel(), true);
        assertNotNull(guestFound.getId());
    }

    @Test
    void buscarTodosHospedes() {
        List<Guest> guests = new ArrayList<>();
        guests.add(guest);
        Mockito.when(guestRepository.findAll()).thenReturn(guests);
        List<Guest> guestsFound = guestService.buscarTodosHospedes();
        assertEquals(guests.size(), guestsFound.size());
        assertEquals(guests.get(0).getNome(), guestsFound.get(0).getNome());
        assertEquals(guests.get(0).getDocumento(), guestsFound.get(0).getDocumento());
        assertEquals(guests.get(0).getTelefone(), guestsFound.get(0).getTelefone());
        assertEquals(guests.get(0).isDentroHotel(), guestsFound.get(0).isDentroHotel());
    }


    @Test
    void buscarHospedeDentroHotel() {
        List<Guest> guests = new ArrayList<>();
        guests.add(guest);
        Mockito.when(guestRepository.findByDentroHotel(true)).thenReturn(Optional.of(guests));
        List<Guest> guestsFound = guestService.buscarHospedeDentroOuForaHotel(true);
        assertEquals(guestsFound.size(), guests.size());
        assertEquals(guestsFound.get(0).getNome(), guests.get(0).getNome());
        assertEquals(guestsFound.get(0).getDocumento(), guests.get(0).getDocumento());
        assertEquals(guestsFound.get(0).getTelefone(), guests.get(0).getTelefone());
        assertEquals(guestsFound.get(0).isDentroHotel(), guests.get(0).isDentroHotel());

    }

    @Test
    void buscarHospedeForaHotel() {
        guest.setDentroHotel(false);
        List<Guest> guests = new ArrayList<>();
        guests.add(guest);
        Mockito.when(guestRepository.findByDentroHotel(false)).thenReturn(Optional.of(guests));
        List<Guest> guestsFound = guestService.buscarHospedeDentroOuForaHotel(false);
        assertEquals(guestsFound.size(), guests.size());
        assertEquals(guestsFound.get(0).getNome(), guests.get(0).getNome());
        assertEquals(guestsFound.get(0).getDocumento(), guests.get(0).getDocumento());
        assertEquals(guestsFound.get(0).getTelefone(), guests.get(0).getTelefone());
        assertEquals(guestsFound.get(0).isDentroHotel(), guests.get(0).isDentroHotel());

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