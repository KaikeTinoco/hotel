package com.desafio.hotel.services;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.repositories.CheckoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela gestão de check-outs de hóspedes.
 *
 * <p>Esta classe fornece operações para criar check-outs, recuperar informações
 * de hóspedes que estão dentro ou fora do hotel, e calcular os custos associados.</p>
 *
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Criar um novo check-out baseado em um check-in</li>
 *   <li>Buscar check-outs por ID do check-in</li>
 *   <li>Listar todos os hóspedes atualmente no hotel</li>
 *   <li>Listar todos os hóspedes que saíram do hotel</li>
 *   <li>Calcular custos totais de hospedagens</li>
 * </ul>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Service
public class CheckoutService {

    private final CheckoutRepository repository;

    private final CheckinService checkinService;

    private final CalculoEstadiaService calculoEstadiaService;

    private final GuestService guestService;

    @Autowired
    public CheckoutService(CheckoutRepository repository, CheckinService checkinService, CalculoEstadiaService calculoEstadiaService, GuestService guestService) {
        this.repository = repository;
        this.checkinService = checkinService;
        this.calculoEstadiaService = calculoEstadiaService;
        this.guestService = guestService;
    }

    /**
     * Cria um novo check-out baseado em um check-in existente.
     *
     * <p>Este método calcula automaticamente o valor total da estadia e marca
     * o hóspede como fora do hotel.</p>
     *
     * @param id identificador único do check-in
     * @return o objeto Checkout criado
     * @throws BadRequestException se o ID for nulo ou o check-in não existir
     */
    public Checkout criarCheckout(Long id){
        if(id == null){
            throw new BadRequestException("id vazio, por favor envie um id válido");
        }
        try {
            Checkin checkin = checkinService.findById(id);
            Guest guest = checkin.getGuest();
            Checkout checkout = new Checkout();
            checkout.setCheckin(checkin);
            checkout.setDataSaida(LocalDateTime.now());
            BigDecimal valorTotal = calculoEstadiaService.calcularValorEstadia(checkin.getDataEntrada(),
                    LocalDateTime.now(), checkin.isAdicionalVeiculo());
            checkout.setValorTotal(valorTotal);
            guest.setDentroHotel(false);
            repository.save(checkout);
            return checkout;
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Busca todos os check-outs associados a um check-in específico.
     *
     * @param id identificador único do check-in
     * @return lista de check-outs do check-in
     * @throws BadRequestException se o ID for nulo ou nenhum check-out for encontrado
     */
    public List<Checkout> findByCheckinId(Long id) {
        if (id == null){
            throw new BadRequestException("Id vazio, por favor informe um id válido");
        }
        try {
            return repository.findByCheckinId(id)
                    .orElseThrow(()-> new BadRequestException("não há nenhum checkout associado a esse cliente"));
        }catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());

        }

    }

    /**
     * Busca todos os hóspedes que estão atualmente dentro do hotel.
     *
     * @return lista de RespostasDTO contendo dados dos hóspedes dentro do hotel
     * @throws BadRequestException se nenhum hóspede for encontrado
     */
    public List<ResponseDTO> buscarTodosHospedesNoHotel() {
        return getResponseDTOS(true);
    }

    /**
     * Busca todos os hóspedes que saíram do hotel.
     *
     * @return lista de RespostasDTO contendo dados dos hóspedes fora do hotel
     * @throws BadRequestException se nenhum hóspede for encontrado
     */
    public List<ResponseDTO> buscarTodosHospedesForaHotel() {
        return getResponseDTOS(false);
    }

    /**
     * Método auxiliar para construir a resposta de hóspedes.
     *
     * <p>Recupera os hóspedes de acordo com sua posição (dentro ou fora do hotel)
     * e calcula o total de despesas para cada um.</p>
     *
     * @param isDentroHotel indica se deve buscar hóspedes dentro (true) ou fora (false) do hotel
     * @return lista de RespostasDTO com informações dos hóspedes e custos
     * @throws BadRequestException se nenhum hóspede for encontrado
     */
    private List<ResponseDTO> getResponseDTOS(boolean isDentroHotel) {
        List<ResponseDTO> response = new ArrayList<>();
        List<Guest> guestList = guestService.buscarHospedeDentroOuForaHotel(isDentroHotel);
        if (guestList == null || guestList.isEmpty()){
            throw new BadRequestException("Hóspedes não econtrados");
        }
        try {
            for(Guest guest: guestList){
                BigDecimal valorGastoTotal;
                BigDecimal valorGastoAtual;
                List<Checkout> todosCheckouts = findByCheckinId(guest.getId());
                if(todosCheckouts.isEmpty()){
                    ResponseDTO responseDTO = ResponseDTO.builder()
                            .guest(guest)
                            .totalUltimaHospedagem(BigDecimal.ZERO)
                            .totalHospedagens(BigDecimal.ZERO)
                            .build();
                    response.add(responseDTO);
                }else{
                    valorGastoTotal = calculoEstadiaService.calcularTotalEstadias(guest.getId(),todosCheckouts);
                    valorGastoAtual = todosCheckouts.get(todosCheckouts.size() - 1).getValorTotal();
                    ResponseDTO responseDTO = ResponseDTO.builder()
                            .guest(guest)
                            .totalHospedagens(valorGastoTotal)
                            .totalUltimaHospedagem(valorGastoAtual)
                            .build();
                    response.add(responseDTO);
                }
            }
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        return response;
    }








}
