package com.desafio.hotel.resource;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.services.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/checkout")
public class CheckoutController {
    @Autowired
    private CheckoutService checkoutService;


    @PostMapping(path = "criarCheckout")
    public ResponseEntity<Checkout> criarCheckout(@RequestParam Long checkinId) throws Exception {
        return ResponseEntity.ok(checkoutService.criarCheckout(checkinId));
    }
}
