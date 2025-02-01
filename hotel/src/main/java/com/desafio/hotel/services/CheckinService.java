package com.desafio.hotel.services;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.exceptions.GuestNotFoundException;
import com.desafio.hotel.repositories.CheckinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckinService {

    private final CheckinRepository repository;


    private final GuestService guestService;

    @Autowired
    public CheckinService(CheckinRepository repository, GuestService guestService) {
        this.repository = repository;
        this.guestService = guestService;
    }


    public Checkin criarCheckin(CheckinCreateDto dto) {
        try {
            Checkin checkin = new Checkin();
            Guest novoHospede = guestService.findById(dto.getGuestId());
            checkin.setGuest(novoHospede);
            checkin.setDataEntrada(dto.getDataEntrada());
            repository.save(checkin);
            return checkin;
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    public String deletarCheckin(Long id) {
        if (id == null){
            throw new BadRequestException("Por favor informe um id válido");
        }
        try {
            Checkin checkin = repository.findById(id).orElseThrow(() ->
                    new BadRequestException("Checkin não encontrado, id inváldo"));
            repository.delete(checkin);
            return "Checkin deletado com sucesso!";
        }catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<Checkin> findByGuestId(Long id)  {
        if (id == null){
            throw new BadRequestException("Por favor informe um id válido");
        }
        return repository.findByGuestId(id).orElseThrow(()
                -> new BadRequestException("não foi possivel encontrar checkins associados a este hospede "));
    }

    public Checkin findById(Long id)  {
        if (id == null){
            throw new BadRequestException("Por favor informe um id válido");
        }
        return repository.findById(id).orElseThrow(()-> new GuestNotFoundException("não há hospede com esse id"));
    }



}



