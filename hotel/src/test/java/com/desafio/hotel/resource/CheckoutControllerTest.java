package com.desafio.hotel.resource;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.checkout.CheckoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes para o controlador de check-out.
 *
 * <p>Verifica as operações de criação e busca de check-outs
 * através dos endpoints REST.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class CheckoutControllerTest {
    @InjectMocks
    private CheckoutController checkoutController;

    @Mock
    private CheckoutServiceImpl checkoutService;

    private Checkout checkout;

    private Guest hospede;

    private Checkin checkin;

    private ResponseDTO dto;

    @BeforeEach
    void inicializar(){
        MockitoAnnotations.openMocks(this);
        hospede = criarHospede();
        checkin = criarCheckin(hospede);
        dto = criarResponseDto(hospede);
        checkout = criarCheckout(checkin);
    }

    @Test
    void criarCheckoutComSucesso() {
        // Arrange
        Checkout checkoutCriado = checkout;
        Checkin checkinCriado = checkin;
        Mockito.when(checkoutService.criarCheckout(checkinCriado.getId()))
                .thenReturn(checkoutCriado);

        // Act
        ResponseEntity response = checkoutController.criarCheckout(checkinCriado.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
    }

    @Test
    void buscarTodosHospedesDentroDoHotelComSucesso() {
        // Arrange
        ResponseDTO responseDTO = dto;
        List<ResponseDTO> dtos = Arrays.asList(responseDTO);
        Mockito.when(checkoutService.buscarTodosHospedesNoHotel()).thenReturn(dtos);

        // Act
        ResponseEntity response = checkoutController.buscarTodosHospedesDentroDoHotel();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
    }

    @Test
    void buscarTodosHospedesForaDoHotelComSucesso() {
        // Arrange
        ResponseDTO responseDTO = dto;
        List<ResponseDTO> dtos = Arrays.asList(responseDTO);
        Mockito.when(checkoutService.buscarTodosHospedesForaHotel()).thenReturn(dtos);

        // Act
        ResponseEntity response = checkoutController.buscarTodosHospedesDentroDoHotel();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
    }

    private Checkout criarCheckout(Checkin checkin){
        Checkout checkout = new Checkout();
        checkout.setCheckin(checkin);
        checkout.setId(1L);
        checkout.getCheckin().setGuest(hospede);
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
        checkin.setDataEntrada(LocalDateTime.now());
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }

    private ResponseDTO criarResponseDto(Guest hospede){
        ResponseDTO dto = ResponseDTO.builder()
                .guest(hospede)
                .totalHospedagens(BigDecimal.valueOf(100))
                .totalUltimaHospedagem(BigDecimal.valueOf(200))
                .build();
        return dto;
    }
}