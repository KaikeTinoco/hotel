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

@Service
public class CheckoutService {


    private final CheckoutRepository repository;


    private final CheckinService checkinService; // Interface injection


    private final CalculoEstadiaService calculoEstadiaService;


    private final GuestService guestService;

    @Autowired
    public CheckoutService(CheckoutRepository repository, CheckinService checkinService, CalculoEstadiaService calculoEstadiaService, GuestService guestService) {
        this.repository = repository;
        this.checkinService = checkinService;
        this.calculoEstadiaService = calculoEstadiaService;
        this.guestService = guestService;
    }


    public Checkout criarCheckout(Long id){
        if(id == null){
            throw new BadRequestException("id vazio, por favor envie um id válido");
        }
        try {
            Checkin checkin = checkinService.findById(id);
            Guest guest = checkin.getGuest();
            Checkout checkout = new Checkout();
            checkout.setGuest(checkin.getGuest());
            checkout.setDataEntrada(checkin.getDataEntrada());
            checkout.setDataSaida(LocalDateTime.now());
            checkout.setAdicionalVeiculo(checkin.isAdicionalVeiculo());
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

    public List<Checkout> findByGuestId(Long id) {
        if (id == null){
            throw new BadRequestException("Id vazio, por favor informe um id válido");
        }
        try {
            return repository.findByGuestId(id)
                    .orElseThrow(()-> new BadRequestException("não há nenhum checkout associado a esse cliente"));
        }catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());

        }

    }

    public List<ResponseDTO> buscarTodosHospedesNoHotel() {
        return getResponseDTOS(true);
    }

    public List<ResponseDTO> buscarTodosHospedesForaHotel() {
        return getResponseDTOS(false);
    }

    private List<ResponseDTO> getResponseDTOS(boolean isDentroHotel) {
        List<ResponseDTO> response = new ArrayList<>();
        List<Guest> guestList = guestService.buscarHospedeDentroOuForaHotel(isDentroHotel);
        if (guestList.isEmpty()){
            throw new BadRequestException("Hóspedes não econtrados");
        }
        try {
            for(Guest guest: guestList){
                BigDecimal valorGastoTotal;
                BigDecimal valorGastoAtual;
                List<Checkout> todosCheckouts = findByGuestId(guest.getId());
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
