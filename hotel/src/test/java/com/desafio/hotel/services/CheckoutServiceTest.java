package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.CheckinRepository;
import com.desafio.hotel.repositories.CheckoutRepository;
import com.desafio.hotel.repositories.GuestRepository;
import org.hibernate.annotations.Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.swing.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {
    @InjectMocks
    private CheckoutService checkoutService;

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private CheckinService checkinService;

    @Mock
    private CheckinRepository checkinRepository;


    @Mock
    private CalculoEstadiaService calculoEstadiaService;

    @Mock
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    private Guest guest;

    private Checkout checkout;

    private GuestDto guestDto;

    private Checkin checkin;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        guest = criarHospede();
        checkout  = criarCheckout(guest);
        guestDto = criarDto();
        checkin = criarCheckin(guest);
    }

    @Test
    void criarCheckout() {
        Checkout checkoutBase = checkout;
        Mockito.when(checkinRepository.save(checkin)).thenReturn(checkin);
        Mockito.when(checkinService.findById(checkin.getId())).thenReturn(checkin);
        Mockito.when(checkoutRepository.save(checkout)).thenReturn(checkout);
        Checkout checkoutCreated = checkoutService.criarCheckout(checkin.getId());



        assertEquals(checkoutBase.getGuest().getNome(), checkoutCreated.getGuest().getNome());
        assertEquals(checkoutBase.getGuest().getDocumento(), checkoutCreated.getGuest().getDocumento());
        assertEquals(checkoutBase.getGuest().getTelefone(), checkoutCreated.getGuest().getTelefone());
        assertEquals(checkoutBase.getDataEntrada(), checkoutCreated.getDataEntrada());
        assertEquals(checkoutBase.isAdicionalVeiculo(), checkoutCreated.isAdicionalVeiculo());
    }

    @Test
    void findByGuestId() {
        Guest guestBase = guest;
        guestRepository.save(guestBase);
        Checkout checkoutBase = checkout;
        List<Checkout> checkouts = Arrays.asList(checkoutBase);
        checkoutRepository.saveAll(checkouts);
        Mockito.when(checkoutService.findByGuestId(guest.getId())).thenReturn(checkouts);
        List<Checkout> checkoutsFound = checkoutService.findByGuestId(guest.getId());
        assertEquals(checkoutBase, checkoutsFound);

    }

    @Test
    void buscarTodosHospedesNoHotel() {
    }

    @Test
    void buscarTodosHospedesForaHotel() {
    }

    private Checkout criarCheckout(Guest guest){
        Checkout checkout = new Checkout();
        checkout.setId(1L);
        checkout.setGuest(guest);
        checkout.setDataEntrada(LocalDateTime.of(2025,1,2,14,0));
        checkout.setDataSaida(LocalDateTime.now());
        checkout.setAdicionalVeiculo(true);
        checkout.setValorTotal(BigDecimal.valueOf(100));
        return checkout;
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

    private GuestDto criarDto(){
        GuestDto dto = new GuestDto();
        dto.setNome("Gustavo");
        dto.setDocumento("23583290");
        dto.setTelefone("111222333444");
        return dto;
    }

    private Checkin criarCheckin(Guest guest){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(guest);
        checkin.setDataEntrada(LocalDateTime.of(2025,1,2,14,0));
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }

}