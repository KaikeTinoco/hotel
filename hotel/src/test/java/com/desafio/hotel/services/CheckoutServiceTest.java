package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.repositories.CheckinRepository;
import com.desafio.hotel.repositories.CheckoutRepository;
import com.desafio.hotel.repositories.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
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

    private ResponseDTO responseDTO;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        guest = criarHospede();
        checkin = criarCheckin(guest);
        checkout  = criarCheckout(checkin);
        responseDTO = criarResponseDTO(guest);
    }

    @Test
    void criarCheckout() {
        Checkout checkoutBase = checkout;
        Mockito.when(checkinRepository.save(checkin)).thenReturn(checkin);
        Mockito.when(checkinService.findById(checkin.getId())).thenReturn(checkin);
        Mockito.when(checkoutRepository.save(checkout)).thenReturn(checkout);
        Checkout checkoutCreated = checkoutService.criarCheckout(checkin.getId());



        assertEquals(checkoutBase.getCheckin().getGuest().getNome(), checkoutCreated.getCheckin().getGuest().getNome());
        assertEquals(checkoutBase.getCheckin().getGuest().getDocumento(), checkoutCreated.getCheckin().getGuest().getDocumento());
        assertEquals(checkoutBase.getCheckin().getGuest().getTelefone(), checkoutCreated.getCheckin().getGuest().getTelefone());
        assertEquals(checkoutBase.getCheckin().getDataEntrada(), checkoutCreated.getCheckin().getDataEntrada());
        assertEquals(checkoutBase.getCheckin().isAdicionalVeiculo(), checkoutCreated.getCheckin().isAdicionalVeiculo());
    }

    @Test
    void findByGuestId() {
        Mockito.when(guestRepository.save(guest)).thenReturn(guest);
        Mockito.when(guestService.findById(guest.getId())).thenReturn(guest);
        Mockito.when(checkoutRepository.findByCheckinId(guest.getId())).thenReturn(Optional.of(Arrays.asList(checkout)));
        List<Checkout> checkins = checkoutService.findByCheckinId(guest.getId());
        assertEquals(checkout, checkins.get(0));
    }

    @Test
    void findByCheckinIdWhenIdNull() {
        assertThrows(BadRequestException.class, () -> checkoutService.findByCheckinId(null));
    }

    @Test
    void buscarTodosHospedesNoHotel() {
        Mockito.when(guestRepository.saveAll(Arrays.asList(guest))).thenReturn(Arrays.asList(guest));
        Mockito.when(guestService.buscarHospedeDentroOuForaHotel(true)).thenReturn(Arrays.asList(guest));
        Mockito.when(checkoutRepository.findByCheckinId(guest.getId())).thenReturn(Optional.of(Arrays.asList(checkout)));
        Mockito.when(calculoEstadiaService.calcularTotalEstadias(guest.getId(),Arrays.asList(checkout)))
                .thenReturn(BigDecimal.valueOf(100));
        List<ResponseDTO> dtos = checkoutService.buscarTodosHospedesNoHotel();
        assertEquals(responseDTO, dtos.get(0));
    }

    @Test
    void buscarTodosHospedesForaHotel() {
        Mockito.when(guestRepository.saveAll(Arrays.asList(guest))).thenReturn(Arrays.asList(guest));
        Mockito.when(guestService.buscarHospedeDentroOuForaHotel(false)).thenReturn(Arrays.asList(guest));
        Mockito.when(checkoutRepository.findByCheckinId(guest.getId())).thenReturn(Optional.of(Arrays.asList(checkout)));
        Mockito.when(calculoEstadiaService.calcularTotalEstadias(guest.getId(),Arrays.asList(checkout)))
                .thenReturn(BigDecimal.valueOf(100));
        List<ResponseDTO> dtos = checkoutService.buscarTodosHospedesForaHotel();
        assertEquals(responseDTO, dtos.get(0));
    }

    @Test
    void buscarTodosHospedesForaDentroHotelIdNull() {
        Mockito.when(guestService.buscarHospedeDentroOuForaHotel(true)).thenReturn(null);
        assertThrows(BadRequestException.class, () -> checkoutService.buscarTodosHospedesNoHotel());
    }

    private Checkout criarCheckout(Checkin checkin){
        Checkout checkout = new Checkout();
        checkout.setId(1L);
        checkout.setCheckin(checkin);
        checkout.setDataSaida(LocalDateTime.now());
        checkout.setValorTotal(BigDecimal.valueOf(100));
        return checkout;
    }

    private Guest criarHospede(){
        Guest guest = new Guest();
        guest.setId(1L);
        guest.setNome("Gustavo");
        guest.setDocumento("183.079.440-07");
        guest.setTelefone("111222333444");
        guest.setDentroHotel(true);
        return guest;
    }

    private Checkin criarCheckin(Guest guest){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(guest);
        checkin.setDataEntrada(LocalDateTime.of(2025,1,2,14,0));
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }

    private ResponseDTO criarResponseDTO(Guest guest){
        return ResponseDTO.builder()
                .guest(guest)
                .totalUltimaHospedagem(BigDecimal.valueOf(100))
                .totalHospedagens(BigDecimal.valueOf(100))
                .build();
    }
}