package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.GuestRepository;
import com.desafio.hotel.services.guests.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o serviço de gerenciamento de hóspedes.
 *
 * <p>Verifica as operações de cadastro, busca, deleção e filtros
 * de hóspedes no sistema hoteleiro.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@ActiveProfiles("test")
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    private Guest hospede;

    private GuestDto dto;

    @BeforeEach
    void inicializar(){
        MockitoAnnotations.initMocks(this);
        hospede = criarHospede();
        dto = criarDto();
    }

    @Test
    void cadastrarHospedeComSucesso() {
        // Arrange
        Mockito.when(this.guestRepository.save(hospede)).thenReturn(hospede);

        // Act
        Guest hospedeSalvo = guestService.cadastrarHospede(dto);

        // Assert
        assertEquals("gustavo", hospedeSalvo.getNome(), 
                "O nome deve ser 'gustavo'");
        assertEquals("18307944007", hospedeSalvo.getDocumento(), 
                "O documento deve ser validado");
        assertEquals("111222333444", hospedeSalvo.getTelefone(), 
                "O telefone deve ser preservado");
        assertTrue(hospedeSalvo.isDentroHotel(), 
                "O hóspede deve estar dentro do hotel por padrão");
    }

    @Test
    void deletarHospedeComSucesso() {
        // Arrange
        hospede.setId(1L);
        Mockito.when(this.guestRepository.findById(1L)).thenReturn(Optional.of(hospede));

        // Act
        String mensagem = guestService.deletarGuestById(hospede.getId());

        // Assert
        assertEquals("Hóspede deletado com sucesso!", mensagem, 
                "A mensagem de sucesso deve ser retornada");
    }

    @Test
    void buscarHospedePorId() {
        // Arrange
        hospede.setId(1L);
        Mockito.when(this.guestRepository.findById(1L)).thenReturn(Optional.of(hospede));

        // Act
        Guest hospedeBuscado = guestService.findById(1L);

        // Assert
        assertEquals("gustavo", hospedeBuscado.getNome(),
                "O nome deve corresponder");
        assertEquals("183.079.440-07", hospedeBuscado.getDocumento(),
                "O documento deve corresponder");
        assertEquals("111222333444", hospedeBuscado.getTelefone(),
                "O telefone deve corresponder");
        assertTrue(hospedeBuscado.isDentroHotel(), "O status dentro/fora do hotel deve ser verdadeiro");
        assertNotNull(hospedeBuscado.getId(), 
                "O ID não pode ser nulo");
    }

    @Test
    void buscarTodosOsHospedes() {
        // Arrange
        List<Guest> hospedes = new ArrayList<>();
        hospedes.add(hospede);
        Mockito.when(guestRepository.findAll()).thenReturn(hospedes);

        // Act
        List<Guest> hospedBuscados = guestService.buscarTodosHospedes();

        // Assert
        assertEquals(hospedes.size(), hospedBuscados.size(), 
                "O tamanho da lista deve ser igual");
        assertEquals(hospedes.get(0).getNome(), hospedBuscados.get(0).getNome(), 
                "O primeiro hóspede deve ter o mesmo nome");
        assertEquals(hospedes.get(0).getDocumento(), hospedBuscados.get(0).getDocumento(), 
                "O primeiro hóspede deve ter o mesmo documento");
        assertEquals(hospedes.get(0).getTelefone(), hospedBuscados.get(0).getTelefone(), 
                "O primeiro hóspede deve ter o mesmo telefone");
        assertEquals(hospedes.get(0).isDentroHotel(), hospedBuscados.get(0).isDentroHotel(), 
                "O primeiro hóspede deve ter o mesmo status");
    }

    @Test
    void buscarHospedeDentroDoHotel() {
        // Arrange
        List<Guest> hospedes = new ArrayList<>();
        hospedes.add(hospede);
        Mockito.when(guestRepository.findByDentroHotel(true)).thenReturn(Optional.of(hospedes));

        // Act
        List<Guest> hospedBuscados = guestService.buscarHospedeDentroOuForaHotel(true);

        // Assert
        assertEquals(hospedBuscados.size(), hospedes.size(), 
                "O tamanho deve ser igual");
        assertEquals(hospedBuscados.get(0).getNome(), hospedes.get(0).getNome(), 
                "O nome deve corresponder");
        assertEquals(hospedBuscados.get(0).getDocumento(), hospedes.get(0).getDocumento(), 
                "O documento deve corresponder");
        assertEquals(hospedBuscados.get(0).getTelefone(), hospedes.get(0).getTelefone(), 
                "O telefone deve corresponder");
        assertEquals(hospedBuscados.get(0).isDentroHotel(), hospedes.get(0).isDentroHotel(), 
                "O status deve corresponder");
    }

    @Test
    void buscarHospedeForaDoHotel() {
        // Arrange
        hospede.setDentroHotel(false);
        List<Guest> hospedes = new ArrayList<>();
        hospedes.add(hospede);
        Mockito.when(guestRepository.findByDentroHotel(false)).thenReturn(Optional.of(hospedes));

        // Act
        List<Guest> hospedBuscados = guestService.buscarHospedeDentroOuForaHotel(false);

        // Assert
        assertEquals(hospedBuscados.size(), hospedes.size(), 
                "O tamanho deve ser igual");
        assertEquals(hospedBuscados.get(0).getNome(), hospedes.get(0).getNome(), 
                "O nome deve corresponder");
        assertEquals(hospedBuscados.get(0).getDocumento(), hospedes.get(0).getDocumento(), 
                "O documento deve corresponder");
        assertEquals(hospedBuscados.get(0).getTelefone(), hospedes.get(0).getTelefone(), 
                "O telefone deve corresponder");
        assertEquals(hospedBuscados.get(0).isDentroHotel(), hospedes.get(0).isDentroHotel(), 
                "O status deve ser falso (fora do hotel)");
    }

    // ...existing code...

    private Guest criarHospede(){
        Guest hospede = new Guest();
        hospede.setNome("gustavo");
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