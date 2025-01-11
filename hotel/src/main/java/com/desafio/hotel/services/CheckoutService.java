package com.desafio.hotel.services;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.CheckoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private CheckoutRepository repository;

    @Autowired
    private CheckinService checkinService; // Interface injection

    @Autowired
    private CalculoEstadiaService calculoEstadiaService;

    @Autowired
    private GuestService guestService;



    public Checkout criarCheckout(Long id) throws Exception {
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
    }

    public List<Checkout> findByGuestId(Long id) throws Exception {
        return repository.findByGuestId(id)
                .orElseThrow(()-> new Exception("não há nenhum checkout associado a esse cliente"));
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
        return response;
    }








}
