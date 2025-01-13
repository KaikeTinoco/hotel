package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import org.hibernate.annotations.Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
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
        List<Checkin> checkins = Arrays.asList(checkinFound);
        checkinRepository.saveAll(checkins);
        List<Checkin> checkinList = this.checkinRepository.findByGuestId(checkin.getId()).get();
        assertEquals(checkinList.size(), checkins.size());
        assertEquals(checkinList.get(0), checkins.get(0));

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
        assertEquals(checkinList.get(0).isAdicionalVeiculo(), true);

    }

    @Test
    void saveCheckin(){
        Checkin checkinFound = checkin;
        guestRepository.save(checkinFound.getGuest());
        checkinRepository.save(checkinFound);
        assertEquals(checkinFound.getGuest().getNome(), "Gustavo");
        assertEquals(checkinFound.getGuest().getTelefone(), "111222333444");
        assertEquals(checkinFound.getGuest().isDentroHotel(), true);
        assertEquals(checkinFound.getGuest().getDocumento(), "23583290");
        assertNotNull(checkinFound.getGuest().getId());
    }

    @Test
    void findByIdTest(){
        guestRepository.save(checkin.getGuest());
        checkinRepository.save(checkin);
        Checkin checkinFound = checkinRepository.findById(checkin.getId()).get();
        assertEquals(checkinFound.getGuest().getNome(), "Gustavo");
        assertEquals(checkinFound.getGuest().getTelefone(), "111222333444");
        assertEquals(checkinFound.getGuest().isDentroHotel(), true);
        assertEquals(checkinFound.getGuest().getDocumento(), "23583290");
        assertNotNull(checkinFound.getId());
        assertEquals(checkinFound.getId(), checkin.getId());

    }

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setNome("Gustavo");
        guest.setDocumento("23583290");
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