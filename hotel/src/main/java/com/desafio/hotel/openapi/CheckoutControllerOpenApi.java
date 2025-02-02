package com.desafio.hotel.openapi;


import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkout.Checkout;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CheckoutControllerOpenApi {
     ResponseEntity<Checkout> criarCheckout(@RequestParam Long checkinId);
     ResponseEntity<List<ResponseDTO>> buscarTodosHospedesDentroDoHotel();
     ResponseEntity<List<ResponseDTO>> buscarTodosHospedesForaDoHotel();
}
