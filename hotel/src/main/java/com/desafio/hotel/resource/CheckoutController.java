package com.desafio.hotel.resource;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.services.CheckoutService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }


    @PostMapping(path = "/criarCheckout")
    public ResponseEntity<Checkout> criarCheckout(@RequestParam Long checkinId) {
        return ResponseEntity.ok(checkoutService.criarCheckout(checkinId));
    }

    @GetMapping(path = "/buscarHospedesDentroHotel")
    public ResponseEntity<List<ResponseDTO>> buscarTodosHospedesDentroDoHotel()  {
        return ResponseEntity.ok(checkoutService.buscarTodosHospedesNoHotel());
    }

    @GetMapping(path = "/buscarHospedesForaHotel")
    public ResponseEntity<List<ResponseDTO>> buscarTodosHospedesForaDoHotel() {
        return ResponseEntity.ok(checkoutService.buscarTodosHospedesForaHotel());
    }
}
