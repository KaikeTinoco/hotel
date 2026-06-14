package com.desafio.hotel.resource;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.checkin.CheckinServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o controlador de check-in.
 *
 * <p>Verifica as operações de criação e deleção de check-ins através
 * do endpoint REST.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class CheckinControllerTest {

    @InjectMocks
    private CheckinController checkinController;

    @Mock
    private CheckinServiceImpl checkinService;

    private Guest hospede;

    private Checkin checkin;

    private CheckinCreateDto criarDto;

    @BeforeEach
    void inicializar(){
        MockitoAnnotations.openMocks(this);
        hospede = criarHospede();
        checkin = criarCheckin(hospede);
        criarDto = criarCheckinDto(hospede);
    }

    @Test
    void criarCheckinComSucesso() {
        // Arrange
        Checkin checkinCriado = checkin;
        CheckinCreateDto checkinDto = criarDto;
        Mockito.when(checkinService.criarCheckin(checkinDto)).thenReturn(checkinCriado);

        // Act
        ResponseEntity response = checkinController.criarCheckin(checkinDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
                "O status da resposta deve ser OK");
        assertNotNull(response.getBody(), 
                "O corpo da resposta não pode ser nulo");
    }

    @Test
    void deletarCheckinComSucesso() {
        // Arrange
        Checkin checkinCriado = checkin;
        Mockito.when(checkinService.deletarCheckin(checkinCriado.getId()))
                .thenReturn("Checkin deletado com sucesso!");

        // Act
        String mensagem = checkinService.deletarCheckin(checkinCriado.getId());

        // Assert
        assertEquals("Checkin deletado com sucesso!", mensagem, 
                "A mensagem de sucesso deve ser retornada");
    }



    // ...existing code...

    private Guest criarHospede(){
        Guest hospede = new Guest();
        hospede.setId(1L);
        hospede.setNome("Gustavo");
        hospede.setDocumento("23583290");
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

    private CheckinCreateDto criarCheckinDto(Guest hospede){
        CheckinCreateDto dto = new CheckinCreateDto();
        dto.setGuestId(hospede.getId());
        dto.setAdicionalVeiculo(true);
        dto.setDataEntrada(LocalDateTime.now());
        return dto;
    }
}