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

/**
 * Serviço responsável pela gestão de check-ins de hóspedes.
 *
 * <p>Esta classe fornece operações para criar, deletar e recuperar check-ins
 * de hóspedes no hotel.</p>
 *
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Criar um novo check-in para um hóspede</li>
 *   <li>Deletar um check-in existente</li>
 *   <li>Buscar check-ins por ID do hóspede ou por ID do check-in</li>
 * </ul>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Service
public class CheckinService {

    private final CheckinRepository repository;

    private final GuestService guestService;

    @Autowired
    public CheckinService(CheckinRepository repository, GuestService guestService) {
        this.repository = repository;
        this.guestService = guestService;
    }

    /**
     * Cria um novo check-in para um hóspede.
     *
     * @param dto objeto contendo os dados do check-in (ID do hóspede, data de entrada e adicional de veículo)
     * @return o objeto Checkin criado
     * @throws BadRequestException se houver erro durante a criação do check-in
     */
    public Checkin criarCheckin(CheckinCreateDto dto) {
        try {
            Checkin checkin = new Checkin();
            Guest novoHospede = guestService.findById(dto.getGuestId());
            checkin.setGuest(novoHospede);
            checkin.setDataEntrada(dto.getDataEntrada());
            checkin.setAdicionalVeiculo(dto.isAdicionalVeiculo());
            repository.save(checkin);
            return checkin;
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Deleta um check-in existente.
     *
     * @param id identificador único do check-in a ser deletado
     * @return mensagem confirmando a exclusão do check-in
     * @throws BadRequestException se o ID for nulo ou o check-in não existir
     */
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

    /**
     * Busca todos os check-ins associados a um hóspede específico.
     *
     * @param id identificador único do hóspede
     * @return lista de check-ins do hóspede
     * @throws BadRequestException se o ID for nulo ou nenhum check-in for encontrado
     */
    public List<Checkin> findByGuestId(Long id)  {
        if (id == null){
            throw new BadRequestException("Por favor informe um id válido");
        }
        return repository.findByGuestId(id).orElseThrow(()
                -> new BadRequestException("não foi possivel encontrar checkins associados a este hospede "));
    }

    /**
     * Busca um check-in específico por ID.
     *
     * @param id identificador único do check-in
     * @return o check-in encontrado
     * @throws BadRequestException se o ID for nulo
     * @throws GuestNotFoundException se o check-in não for encontrado
     */
    public Checkin findById(Long id)  {
        if (id == null){
            throw new BadRequestException("Por favor informe um id válido");
        }
        return repository.findById(id).orElseThrow(()-> new GuestNotFoundException("não há hospede com esse id"));
    }



}
