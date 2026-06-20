package com.desafio.hotel.services.checkout;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkout.Checkout;
import java.util.List;

/**
 * Interface para o serviço de Checkout. Implementada por CheckoutServiceImpl.
 */
public interface CheckoutService {
    Checkout criarCheckout(Long id);
    List<Checkout> findByCheckinId(Long id);
    List<ResponseDTO> buscarTodosHospedesNoHotel();
    List<ResponseDTO> buscarTodosHospedesForaHotel();
    List<Checkout> listarTodosCheckoutsDoCliente(Long clienteId);
}

