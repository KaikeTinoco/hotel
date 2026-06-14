package com.desafio.hotel.services.guests;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import java.util.List;

/**
 * Interface para o serviço de hóspedes. Implementada por GuestServiceImpl.
 */
public interface GuestService {
    Guest cadastrarHospede(GuestDto dto);
    List<Guest> filtroDeBusca(String nome, String documento, String telefone);
    String deletarGuestById(Long id);
    Guest findById(Long guestId);
    List<Guest> buscarTodosHospedes();
    List<Guest> buscarHospedeDentroOuForaHotel(boolean dentroHotel);
}

