package com.desafio.hotel.resource;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o controlador de hóspedes.
 *
 * <p>Verifica as operações de cadastro, busca e deleção de hóspedes
 * através dos endpoints REST.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class GuestControllerTest {
    @InjectMocks
    private GuestController guestController;

    @Mock
    private GuestService guestService;

    private Guest hospede;

    private GuestDto dto;

    private Checkin checkin;

    @BeforeEach
    void inicializar(){
        MockitoAnnotations.initMocks(this);
        hospede = criarHospede();
        checkin = criarCheckin(hospede);
        dto = criarDto();
    }

    @Test
    void cadastrarHospedeComSucesso() {
        // Arrange
        Guest hospedeCadastrado = hospede;
        hospedeCadastrado.setId(1L);
        Mockito.when(guestService.cadastrarHospede(dto)).thenReturn(hospedeCadastrado);

        // Act
        ResponseEntity response = guestController.cadastrarHospede(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
    }

    @Test
    void buscarTodosHospedesComSucesso() {
        // Arrange
        List<Guest> hospedes = Arrays.asList(hospede);
        Mockito.when(guestService.buscarTodosHospedes()).thenReturn(hospedes);

        // Act
        ResponseEntity response = guestController.buscarTodosHospedes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
        assertNotNull(response.getBody(),
                "O corpo da resposta não pode ser nulo");
    }

    @Test
    void deletarHospedeComSucesso() {
        // Arrange
        Guest hospedeDeletado = hospede;
        hospedeDeletado.setId(1L);
        Mockito.when(guestService.cadastrarHospede(dto)).thenReturn(hospedeDeletado);
        Mockito.when(guestService.findById(hospedeDeletado.getId())).thenReturn(hospedeDeletado);

        // Act
        ResponseEntity response = guestController.deletarHospede(hospedeDeletado.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "O status deve ser OK");
    }

    // ...existing code...

    private Checkout criarCheckout(){
        Guest hospede = new Guest(1L, "Pedro", "000.240.470-23", "6724581391", true);
        Checkout checkout = new Checkout();
        checkout.setCheckin(criarCheckin(hospede));
        checkout.getCheckin().setGuest(hospede);
        checkout.getCheckin().setAdicionalVeiculo(false);
        checkout.getCheckin().setDataEntrada(LocalDateTime.of(2025,1, 20, 8, 30));
        checkout.setDataSaida(LocalDateTime.of(2025,1,23,17,0));
        return checkout;
    }

    private Checkin criarCheckin(Guest hospede){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(hospede);
        checkin.setDataEntrada(LocalDateTime.now());
        checkin.setAdicionalVeiculo(true);
        return checkin;
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

    private GuestDto criarDto(){
        GuestDto dto = new GuestDto();
        dto.setNome("gustavo");
        dto.setDocumento("183.079.440-07");
        dto.setTelefone("111222333444");
        return dto;
    }
}