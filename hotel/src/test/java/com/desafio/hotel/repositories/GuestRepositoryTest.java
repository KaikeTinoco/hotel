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
        guestRepository.save(guest);
    }

    @Test
    void findByDentroHotel() {
        Optional<List<Guest>> guests = guestRepository.findByDentroHotel(true);
        assertNotNull(guests);
        assertThat(!guests.get().isEmpty());
    }

    @Test
    void findByDentroHotelError() {
        Optional<List<Guest>> guests = guestRepository.findByDentroHotel(false);
        assertNotNull(guests);
        assertThat(guests.get().isEmpty());
    }

    @Test
    void saveGuestTest(){
        Guest created = criarHospede();
        guestRepository.save(created);
    }

    @Test
    void findByIdTest(){
        Optional<Guest> foundGuest = guestRepository.findById(guest.getId());
        assertEquals(foundGuest.get(), guest);
    }

    @Test
    void buscarTodosHospedes(){
        List<Guest> foundGuests = guestRepository.findAll();
        assertThat(guest).isNotNull();
    }

    @Test
    void  deletarGuestByIdTest(){
        Optional<Guest> optionalGuest = guestRepository.findById(guestRepository.findAll().get(0).getId());
        guestRepository.delete(optionalGuest.get());
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