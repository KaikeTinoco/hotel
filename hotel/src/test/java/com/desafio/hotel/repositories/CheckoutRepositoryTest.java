package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CheckoutRepositoryTest {
    private final CheckinRepository checkinRepository;
    private final GuestRepository guestRepository;
    private final CheckoutRepository checkoutRepository;


    private Guest guest;

    private Checkout checkout;

    private Checkin checkin;

    @Autowired
    CheckoutRepositoryTest(CheckinRepository checkinRepository, GuestRepository guestRepository, CheckoutRepository checkoutRepository) {
        this.checkinRepository = checkinRepository;
        this.guestRepository = guestRepository;
        this.checkoutRepository = checkoutRepository;
    }

    @BeforeEach
    void init(){
        guest = criarHospede();
        checkin = criarCheckin(guest);
        checkout = criarCheckout(checkin);
    }

    @Test
    void findById() {
        Guest guestCreated = guest;
        Checkout checkoutCreated = checkout;
        Checkin checkinlocal = checkin;
        guestRepository.save(guestCreated);
        checkinRepository.save(checkinlocal);
        checkoutRepository.save(checkoutCreated);
        assertEquals("gustavo", checkoutCreated.getCheckin().getGuest().getNome());
        assertEquals("183.079.440-07", checkoutCreated.getCheckin().getGuest().getDocumento());
        assertEquals("111222333444", checkoutCreated.getCheckin().getGuest().getTelefone());
        assertTrue(checkoutCreated.getCheckin().getGuest().isDentroHotel());
        assertEquals(1L, checkout.getId());
        assertEquals(BigDecimal.valueOf(100), checkoutCreated.getValorTotal());
        assertTrue(checkoutCreated.getCheckin().isAdicionalVeiculo());
        assertEquals(LocalDateTime.of(2026, 2, 13, 13, 10),
                checkoutCreated.getCheckin().getDataEntrada());

    }

    @Test
    void findByGuestId() {
        Guest guestCreated = guest;
        Checkin checkinl = checkin;
        guestRepository.save(guestCreated);
        checkinRepository.save(checkinl);
        Checkout checkooutCreated = checkout;
        List<Checkout> checkouts = Arrays.asList(checkooutCreated);
        checkoutRepository.saveAll(checkouts);
        Optional<List<Checkout>> checkoutsFound = checkoutRepository.findByCheckinId(guestCreated.getId());
        assertEquals(checkouts.size(), checkoutsFound.get().size());
        assertEquals(checkouts.get(0).getCheckin(), checkoutsFound.get().get(0).getCheckin());
        assertEquals(checkouts.get(0).getValorTotal(), checkoutsFound.get().get(0).getValorTotal());

    }


    @Test
    void saveCheckoutTest(){
        Guest guestCreated = guest;
        Checkin checkinl = checkin;
        guestRepository.save(guestCreated);
        checkinRepository.save(checkinl);
        Checkout checkoutCreated = checkout;
        checkoutRepository.save(checkoutCreated);
        assertEquals("gustavo", checkoutCreated.getCheckin().getGuest().getNome());
        assertEquals("183.079.440-07", checkoutCreated.getCheckin().getGuest().getDocumento());
        assertEquals("111222333444", checkoutCreated.getCheckin().getGuest().getTelefone());
        assertEquals(BigDecimal.valueOf(100), checkoutCreated.getValorTotal());
        assertTrue(checkoutCreated.getCheckin().isAdicionalVeiculo());
        assertEquals(LocalDateTime.of(2026, 2, 13, 13, 10), checkoutCreated.getCheckin().getDataEntrada());
    }

    @Test
    void deleteCheckoutTest(){
        Guest guestCreated = guest;
        Checkin checkinl = checkin;
        guestRepository.save(guestCreated);
        checkinRepository.save(checkinl);
        Checkout checkoutCreated = checkout;
        checkoutRepository.save(checkoutCreated);
        checkoutRepository.delete(checkoutCreated);
        assertFalse(checkoutRepository.findById(checkoutCreated.getId()).isPresent());
    }

    private Checkout criarCheckout(Checkin checkin){
        Checkout checkout = new Checkout();
        checkout.setCheckin(checkin);
        checkout.setDataSaida(LocalDateTime.of(2026, 2, 14, 13, 10));
        checkout.setValorTotal(BigDecimal.valueOf(100));
        return checkout;
    }

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setNome("gustavo");
        guest.setDocumento("183.079.440-07");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }

    private Checkin criarCheckin(Guest guest){
        Checkin checkin = new Checkin();
        checkin.setGuest(guest);
        checkin.setDataEntrada(LocalDateTime.of(2026, 2, 13, 13, 10));
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }
}