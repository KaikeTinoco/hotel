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
import com.desafio.hotel.services.checkin.CheckinServiceImpl;
import com.desafio.hotel.services.checkout.CheckoutServiceImpl;
import com.desafio.hotel.services.estadia.CalculoEstadiaServiceImpl;
import com.desafio.hotel.services.guests.GuestServiceImpl;
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

/**
 * Testes para o serviço de gerenciamento de check-outs.
 *
 * <p>Verifica as operações de criação, busca e relatório de check-outs
 * dos hóspedes do hotel.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class CheckoutServiceTest {
    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private CheckinServiceImpl checkinService;

    @Mock
    private CheckinRepository checkinRepository;

    @Mock
    private CalculoEstadiaServiceImpl calculoEstadiaService;

    @Mock
    private GuestServiceImpl guestService;

    @Mock
    private GuestRepository guestRepository;

    private Guest hospede;

    private Checkout checkout;

    private GuestDto guestDto;

    private Checkin checkin;

    private ResponseDTO responseDTO;

    @BeforeEach
    void inicializar(){
        MockitoAnnotations.openMocks(this);
        hospede = criarHospede();
        checkin = criarCheckin(hospede);
        checkout  = criarCheckout(checkin);
        responseDTO = criarResponseDTO(hospede);
    }

    @Test
    void criarCheckoutComSucesso() {
        // Arrange
        Checkout checkoutBase = checkout;
        Mockito.when(checkinRepository.save(checkin)).thenReturn(checkin);
        Mockito.when(checkinService.findById(checkin.getId())).thenReturn(checkin);
        Mockito.when(checkoutRepository.save(checkout)).thenReturn(checkout);

        // Act
        Checkout checkoutCriado = checkoutService.criarCheckout(checkin.getId());

        // Assert
        assertEquals(checkoutBase.getCheckin().getGuest().getNome(), 
                checkoutCriado.getCheckin().getGuest().getNome(), 
                "O nome do hóspede deve corresponder");
        assertEquals(checkoutBase.getCheckin().getGuest().getDocumento(), 
                checkoutCriado.getCheckin().getGuest().getDocumento(), 
                "O documento do hóspede deve corresponder");
        assertEquals(checkoutBase.getCheckin().getGuest().getTelefone(), 
                checkoutCriado.getCheckin().getGuest().getTelefone(), 
                "O telefone do hóspede deve corresponder");
        assertEquals(checkoutBase.getCheckin().getDataEntrada(), 
                checkoutCriado.getCheckin().getDataEntrada(), 
                "A data de entrada deve corresponder");
        assertEquals(checkoutBase.getCheckin().isAdicionalVeiculo(), 
                checkoutCriado.getCheckin().isAdicionalVeiculo(), 
                "O adicional de veículo deve corresponder");
    }

    @Test
    void buscarCheckoutPorHospedeId() {
        // Arrange
        Mockito.when(guestRepository.save(hospede)).thenReturn(hospede);
        Mockito.when(guestService.findById(hospede.getId())).thenReturn(hospede);
        Mockito.when(checkoutRepository.findByCheckinId(hospede.getId()))
                .thenReturn(Optional.of(Arrays.asList(checkout)));

        // Act
        List<Checkout> checkouts = checkoutService.findByCheckinId(hospede.getId());

        // Assert
        assertEquals(checkout, checkouts.get(0), 
                "O primeiro checkout deve corresponder");
    }

    @Test
    void buscarCheckoutPorIdNulo() {
        // Act & Assert
        assertThrows(BadRequestException.class, 
                () -> checkoutService.findByCheckinId(null), 
                "Deve lançar exceção para ID nulo");
    }

    @Test
    void buscarTodosOsHospedesDentroHotel() {
        // Arrange
        Mockito.when(guestRepository.saveAll(Arrays.asList(hospede))).thenReturn(Arrays.asList(hospede));
        Mockito.when(guestService.buscarHospedeDentroOuForaHotel(true))
                .thenReturn(Arrays.asList(hospede));
        Mockito.when(checkoutRepository.findByCheckinId(hospede.getId()))
                .thenReturn(Optional.of(Arrays.asList(checkout)));
        Mockito.when(calculoEstadiaService.calcularTotalEstadias(hospede.getId(), Arrays.asList(checkout)))
                .thenReturn(BigDecimal.valueOf(100));

        // Act
        List<ResponseDTO> dtos = checkoutService.buscarTodosHospedesNoHotel();

        // Assert
        assertEquals(responseDTO, dtos.get(0), 
                "O primeiro DTO deve corresponder");
    }

    @Test
    void buscarTodosOsHuspedesForaHotel() {
        // Arrange
        Mockito.when(guestRepository.saveAll(Arrays.asList(hospede))).thenReturn(Arrays.asList(hospede));
        Mockito.when(guestService.buscarHospedeDentroOuForaHotel(false))
                .thenReturn(Arrays.asList(hospede));
        Mockito.when(checkoutRepository.findByCheckinId(hospede.getId()))
                .thenReturn(Optional.of(Arrays.asList(checkout)));
        Mockito.when(calculoEstadiaService.calcularTotalEstadias(hospede.getId(), Arrays.asList(checkout)))
                .thenReturn(BigDecimal.valueOf(100));

        // Act
        List<ResponseDTO> dtos = checkoutService.buscarTodosHospedesForaHotel();

        // Assert
        assertEquals(responseDTO, dtos.get(0), 
                "O primeiro DTO deve corresponder");
    }

    @Test
    void buscarHospedesComIdNulo() {
        // Arrange
        Mockito.when(guestService.buscarHospedeDentroOuForaHotel(true)).thenReturn(null);

        // Act & Assert
        assertThrows(BadRequestException.class, 
                () -> checkoutService.buscarTodosHospedesNoHotel(), 
                "Deve lançar exceção quando lista é nula");
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
        Guest hospede = new Guest();
        hospede.setId(1L);
        hospede.setNome("Gustavo");
        hospede.setDocumento("183.079.440-07");
        hospede.setTelefone("111222333444");
        hospede.setDentroHotel(true);
        return hospede;
    }

    private Checkin criarCheckin(Guest hospede){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(hospede);
        checkin.setDataEntrada(LocalDateTime.of(2025,1,2,14,0));
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }

    private ResponseDTO criarResponseDTO(Guest hospede){
        return ResponseDTO.builder()
                .guest(hospede)
                .totalUltimaHospedagem(BigDecimal.valueOf(100))
                .totalHospedagens(BigDecimal.valueOf(100))
                .build();
    }
}