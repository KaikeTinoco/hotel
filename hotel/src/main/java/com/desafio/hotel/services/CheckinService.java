package com.desafio.hotel.services;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.CheckinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckinService {
    @Autowired
    private CheckinRepository repository;

    @Autowired
    private GuestService guestService;

    public Checkin criarCheckin(CheckinCreateDto dto) throws Exception {
        Checkin checkin = new Checkin();
        Guest novoHospede = guestService.findById(dto.getGuestId());
        checkin.setGuest(novoHospede);
        checkin.setDataEntrada(dto.getDataEntrada());
        repository.save(checkin);
        return checkin;
    }

    public String deletarCheckin(Long id) throws Exception {
        Checkin checkin = repository.findById(id).orElseThrow(() -> new Exception("Hospede não encontrado, id inváldo"));
        return "Checkin deletado com sucesso!";
    }

    public List<Checkin> findByGuestId(Long id) throws Exception {
        return repository.findByGuestId(id).orElseThrow(() -> new Exception("não foi possivel encontrar "));
    }

    public Checkin findById(Long id) throws Exception {
        return repository.findById(id).orElseThrow(()-> new Exception("não há hospede com esse id"));
    }



}



