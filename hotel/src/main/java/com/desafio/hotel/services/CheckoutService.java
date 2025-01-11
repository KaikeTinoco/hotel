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


    public Checkout criarCheckout(Long id) throws Exception {
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
            throw new BadRequestException("erro ao criar o checkout, por favor contate o suporte");
        }
    }

    public List<Checkout> findByGuestId(Long id) throws Exception {
        if (id == null){
            throw new BadRequestException("Id vazio, por favor informe um id válido");
        }
        try {
            return repository.findByGuestId(id)
                    .orElseThrow(()-> new Exception("não há nenhum checkout associado a esse cliente"));
        }catch (BadRequestException e){
            throw new BadRequestException("erro ao buscar o hóspede, por favor contate o suporte");
        }

    }

    public List<ResponseDTO> buscarTodosHospedesNoHotel() throws Exception {
        return getResponseDTOS(true);
    }

    public List<ResponseDTO> buscarTodosHospedesForaHotel() throws Exception {
        return getResponseDTOS(false);
    }

    private List<ResponseDTO> getResponseDTOS(boolean isDentroHotel) throws Exception {
        List<ResponseDTO> response = new ArrayList<>();
        List<Guest> guestList = guestService.buscarHospedeDentroOuForaHotel(isDentroHotel);
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
                    valorGastoTotal = todosCheckouts.stream()
                            .map(Checkout::getValorTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
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
            throw new BadRequestException
                    ("Erro ao calcular os valores gastos pelo hóspede, por favor contate o suporte");
        }
        return response;
    }








}
