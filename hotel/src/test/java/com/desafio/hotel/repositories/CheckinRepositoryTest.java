package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CheckinRepositoryTest {
    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private GuestRepository guestRepository;


    private Checkin checkin;

    @BeforeEach
    void init(){
        checkin = criarCheckin();
    }

    @Test
    void findByGuestId() {
        Checkin checkinFound = checkin;
        guestRepository.save(checkinFound.getGuest());
        checkinRepository.save(checkinFound);

        List<Checkin> checkins = Arrays.asList(checkinFound);

        List<Checkin> checkinList = this.checkinRepository
                .findByGuestId(checkinFound.getGuest().getId())
                .orElse(Collections.emptyList());

        assertEquals(checkins.size(), checkinList.size());
        assertEquals(checkins.get(0), checkinList.get(0));
    }

    @Test
    void findByAdicionalVeiculo() {
        Checkin checkinFound = checkin;
        guestRepository.save(checkinFound.getGuest());
        List<Checkin> checkins = Arrays.asList(checkinFound);
        checkinRepository.saveAll(checkins);
        List<Checkin> checkinList = this.checkinRepository.findByAdicionalVeiculo(true).get();
        assertEquals(checkinList.size(), checkins.size());
        assertEquals(checkinList.get(0), checkins.get(0));
        assertTrue(checkinList.get(0).isAdicionalVeiculo());

    }

    @Test
    void saveCheckin(){
        Checkin checkinFound = checkin;
        guestRepository.save(checkinFound.getGuest());
        checkinRepository.save(checkinFound);
        assertEquals("gustavo", checkinFound.getGuest().getNome());
        assertEquals("111222333444", checkinFound.getGuest().getTelefone());
        assertTrue(checkinFound.getGuest().isDentroHotel());
        assertEquals("18307944007", checkinFound.getGuest().getDocumento());
        assertNotNull(checkinFound.getGuest().getId());
    }

    @Test
    void findByIdTest(){
        guestRepository.save(checkin.getGuest());
        checkinRepository.save(checkin);
        Checkin checkinFound = checkinRepository.findById(checkin.getId()).get();
        assertEquals("gustavo", checkinFound.getGuest().getNome());
        assertEquals("111222333444", checkinFound.getGuest().getTelefone());
        assertTrue(checkinFound.getGuest().isDentroHotel());
        assertEquals("18307944007", checkinFound.getGuest().getDocumento());
        assertNotNull(checkinFound.getId());
        assertEquals(checkinFound.getId(), checkin.getId());

    }

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setNome("gustavo");
        guest.setDocumento("18307944007");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }

    private Checkin criarCheckin(){
        Checkin checkin = new Checkin();
        checkin.setGuest(criarHospede());
        checkin.setDataEntrada(LocalDateTime.now());
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }
}