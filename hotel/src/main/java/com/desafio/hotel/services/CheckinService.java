package com.desafio.hotel.services;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.entity.checkin.Checkin;
import java.util.List;

/**
 * Interface para o serviço de Checkin. Define as operações públicas esperadas
 * pela aplicação. Implementada por CheckinServiceImpl.
 */
public interface CheckinService {
    Checkin criarCheckin(CheckinCreateDto dto);
    String deletarCheckin(Long id);
    List<Checkin> findByGuestId(Long id);
    Checkin findById(Long id);
}
