package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.GuestNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GuestRepositoryTest {
    @Autowired
    private GuestRepository guestRepository;

    private Guest guest;

    @BeforeEach
    public void init(){
        guest = criarHospede();
    }

    @Test
    void findByDentroHotel() {
        guestRepository.save(guest);
        Optional<List<Guest>> guests = guestRepository.findByDentroHotel(true);
        assertNotNull(guests);
        assertThat(!guests.get().isEmpty());
    }

    @Test
    void findByDentroHotelError() {
        guestRepository.save(guest);
        Optional<List<Guest>> guests = guestRepository.findByDentroHotel(false);
        assertNotNull(guests);
        assertThat(guests.get().isEmpty());
    }

    @Test
    void  deletarGuestByIdTest(){
        guestRepository.save(guest);
        Optional<Guest> optionalGuest = guestRepository.findById(guest.getId());
        guestRepository.delete(optionalGuest.get());
    }

    @Test
    void saveGuestTest(){
        guestRepository.save(guest);
        assertThat(guest.getNome()).isEqualTo("Gustavo");
        assertThat(guest.getDocumento()).isEqualTo("23583290");
        assertThat(guest.getTelefone()).isEqualTo("111222333444");
        assertThat(guest.isDentroHotel()).isEqualTo(true);
        assertNotNull(guest.getId());
        assertEquals(guest.getId(), Long.valueOf(5));
    }

    @Test
    void findByIdTest(){
        guestRepository.save(guest);
        Optional<Guest> foundGuest = guestRepository.findById(guest.getId());
        assertEquals(foundGuest.get().getNome(), "Gustavo");
        assertEquals(foundGuest.get().getDocumento(),"23583290");
        assertEquals(foundGuest.get().getTelefone(),"111222333444");
        assertEquals(foundGuest.get().isDentroHotel(), true);
        assertNotNull(foundGuest.get().getId());
        assertEquals(foundGuest.get().getId(), Long.valueOf(4));
    }

    @Test
    void buscarTodosHospedes(){
        List<Guest> guests = Arrays.asList(guest);
        guestRepository.saveAll(guests);
        List<Guest> foundGuests = guestRepository.findAll();
        assertThat(guest).isNotNull();

    }

    @Test
    void guestNotFoundTest(){
        Optional<Guest> guestOptional = guestRepository.findById(Long.MAX_VALUE);
        assertFalse(guestOptional.isPresent());
    }




    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setNome("Gustavo");
        guest.setDocumento("23583290");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }
}