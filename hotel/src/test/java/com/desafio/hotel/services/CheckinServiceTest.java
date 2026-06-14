package com.desafio.hotel.services;
import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.repositories.CheckinRepository;
import com.desafio.hotel.repositories.GuestRepository;
import com.desafio.hotel.services.checkin.CheckinServiceImpl;
import com.desafio.hotel.services.guests.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o serviço de gerenciamento de check-ins.
 *
 * <p>Verifica as operações de criação, deleção e busca de check-ins
 * de hóspedes no hotel.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class CheckinServiceTest {
    @InjectMocks
    private CheckinServiceImpl checkinService;
    @Mock
    private CheckinRepository checkinRepository;
    @Mock
    private GuestServiceImpl guestService;
    @Mock
    private GuestRepository guestRepository;
    private Guest hospede;
    private Checkin checkin;
    private CheckinCreateDto checkinDto;
    private GuestDto dto;
    
    @BeforeEach
    void inicializar(){
        MockitoAnnotations.openMocks(this);
        hospede = criarHospede();
        checkin = criarCheckin();
        dto = criarDto();
        checkinDto = criarCheckinDto(hospede);
    }

    @Test
    void criarCheckinComSucesso() {
        // Arrange
        Mockito.when(guestService.findById(checkinDto.getGuestId())).thenReturn(hospede);
        Mockito.when(checkinRepository.save(Mockito.any(Checkin.class))).thenReturn(checkin);

        // Act
        Checkin checkinCriado = checkinService.criarCheckin(checkinDto);

        // Assert
        assertEquals(hospede, checkinCriado.getGuest(), 
                "O hóspede do check-in deve ser o mesmo");
    }

    @Test
    void deletarCheckinComSucesso() {
        // Arrange
        Mockito.when(checkinRepository.findById(checkin.getId())).thenReturn(Optional.of(checkin));

        // Act
        String mensagem = checkinService.deletarCheckin(checkin.getId());

        // Assert
        assertEquals("Checkin deletado com sucesso!", mensagem, 
                "A mensagem de sucesso deve ser retornada");
    }

    @Test
    void deletarCheckinComIdNulo() {
        // Act & Assert
        BadRequestException excecao = assertThrows(BadRequestException.class, 
                () -> checkinService.deletarCheckin(null));
        assertEquals("Por favor informe um id válido", excecao.getMessage(), 
                "A mensagem de erro deve ser específica");
    }

    @Test
    void buscarCheckinPorHospedeId() {
        // Arrange
        Mockito.when(guestRepository.save(hospede)).thenReturn(hospede);
        Mockito.when(guestService.findById(hospede.getId())).thenReturn(hospede);
        Mockito.when(checkinRepository.findByGuestId(hospede.getId())).thenReturn(Optional.of(Arrays.asList(checkin)));

        // Act
        List<Checkin> checkins = checkinService.findByGuestId(hospede.getId());

        // Assert
        assertEquals(checkin, checkins.get(0), 
                "O primeiro check-in deve corresponder");
    }

    @Test
    void buscarCheckinPorId() {
        // Arrange
        Mockito.when(checkinRepository.findById(checkin.getId())).thenReturn(Optional.of(checkin));

        // Act
        Checkin checkinBuscado = checkinService.findById(checkin.getId());

        // Assert
        assertEquals(checkin, checkinBuscado, 
                "O check-in buscado deve ser igual");
    }

    @Test
    void buscarCheckinPorIdComIdNulo(){
        // Act & Assert
        BadRequestException excecao = assertThrows(BadRequestException.class, 
                () -> checkinService.findById(null));
        assertEquals("Por favor informe um id válido", excecao.getMessage(), 
                "A mensagem de erro deve ser específica");
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
    private Checkin criarCheckin(){
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setGuest(criarHospede());
        checkin.setDataEntrada(LocalDateTime.now());
        checkin.setAdicionalVeiculo(true);
        return checkin;
    }
    private GuestDto criarDto(){
        GuestDto dto = new GuestDto();
        dto.setNome("Gustavo");
        dto.setDocumento("183.079.440-07");
        dto.setTelefone("111222333444");
        return dto;
    }
    private CheckinCreateDto criarCheckinDto(Guest hospede){
        CheckinCreateDto dto = new CheckinCreateDto();
        dto.setGuestId(hospede.getId());
        dto.setAdicionalVeiculo(true);
        dto.setDataEntrada(LocalDateTime.now());
        return dto;
    }
}