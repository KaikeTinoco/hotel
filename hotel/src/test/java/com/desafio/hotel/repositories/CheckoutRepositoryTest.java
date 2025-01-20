package com.desafio.hotel.repositories;

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
    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private GuestRepository guestRepository;

    private Guest guest;

    private Checkout checkout;

    @BeforeEach
    void init(){
        guest = criarHospede();
        checkout = criarCheckout(guest);
    }

    @Test
    void findById() {
        Guest guestCreated = guest;
        Checkout checkoutCreated = checkout;
        guestRepository.save(guestCreated);
        checkoutRepository.save(checkoutCreated);
        assertEquals("Gustavo", checkoutCreated.getGuest().getNome());
        assertEquals("23583290", checkoutCreated.getGuest().getDocumento());
        assertEquals("111222333444", checkoutCreated.getGuest().getTelefone());
        assertTrue(checkoutCreated.getGuest().isDentroHotel());
        assertEquals(1L, checkout.getId());
        assertEquals(BigDecimal.valueOf(100), checkoutCreated.getValorTotal());
        assertTrue(checkoutCreated.isAdicionalVeiculo());
        assertEquals(LocalDateTime.of(2025,1,2,14,0),
                checkoutCreated.getDataEntrada());

    }

    @Test
    void findByGuestId() {
        Guest guestCreated = guest;
        guestRepository.save(guest);
        Checkout checkooutCreated = checkout;
        List<Checkout> checkouts = Arrays.asList(checkooutCreated);
        checkoutRepository.saveAll(checkouts);
        Optional<List<Checkout>> checkoutsFound = checkoutRepository.findByGuestId(guestCreated.getId());
        assertEquals(checkouts.size(), checkoutsFound.get().size());
        assertEquals(checkouts.get(0).getGuest(), checkoutsFound.get().get(0).getGuest());
        assertEquals(checkouts.get(0).getValorTotal(), checkoutsFound.get().get(0).getValorTotal());

    }


    @Test
    void saveCheckoutTest(){
        Guest guestCreated = guest;
        guestRepository.save(guestCreated);
        Checkout checkoutCreated = checkout;
        checkoutRepository.save(checkoutCreated);
        assertEquals("Gustavo", checkoutCreated.getGuest().getNome());
        assertEquals("23583290", checkoutCreated.getGuest().getDocumento());
        assertEquals("111222333444", checkoutCreated.getGuest().getTelefone());
        assertEquals(BigDecimal.valueOf(100), checkoutCreated.getValorTotal());
        assertTrue(checkoutCreated.isAdicionalVeiculo());
        assertEquals(LocalDateTime.of(2025,1,2,14,0), checkoutCreated.getDataEntrada());
    }

    @Test
    void deleteCheckoutTest(){
        Guest guestCreated = guest;
        guestRepository.save(guestCreated);
        Checkout checkoutCreated = checkout;
        checkoutRepository.save(checkoutCreated);
        checkoutRepository.delete(checkoutCreated);
        assertFalse(checkoutRepository.findById(checkoutCreated.getId()).isPresent());
    }

    private Checkout criarCheckout(Guest guest){
        Checkout checkout = new Checkout();
        checkout.setGuest(guest);
        checkout.setDataEntrada(LocalDateTime.of(2025,1,2,14,0));
        checkout.setDataSaida(LocalDateTime.now());
        checkout.setAdicionalVeiculo(true);
        checkout.setValorTotal(BigDecimal.valueOf(100));
        return checkout;
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