package com.desafio.hotel.resource;

import com.desafio.hotel.dto.response.ResponseDTO;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import com.desafio.hotel.openapi.CheckoutControllerOpenApi;
import com.desafio.hotel.services.CheckoutService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de check-outs.
 *
 * <p>Fornece endpoints para criar check-outs e buscar informações de hóspedes
 * dentro ou fora do hotel.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/checkout")
public class CheckoutController implements CheckoutControllerOpenApi {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    /**
     * Cria um novo check-out para um hóspede.
     *
     * @param checkinId identificador do check-in
     * @return ResponseEntity contendo o check-out criado
     */
    @PostMapping(path = "/criarCheckout")
    public ResponseEntity<Checkout> criarCheckout(@RequestParam Long checkinId) {
        return ResponseEntity.ok(checkoutService.criarCheckout(checkinId));
    }

    /**
     * Busca todos os hóspedes atualmente dentro do hotel.
     *
     * @return ResponseEntity com lista de hóspedes dentro do hotel
     */
    @GetMapping(path = "/buscarHospedesDentroHotel")
    public ResponseEntity<List<ResponseDTO>> buscarTodosHospedesDentroDoHotel()  {
        return ResponseEntity.ok(checkoutService.buscarTodosHospedesNoHotel());
    }

    /**
     * Busca todos os hóspedes que saíram do hotel.
     *
     * @return ResponseEntity com lista de hóspedes fora do hotel
     */
    @GetMapping(path = "/buscarHospedesForaHotel")
    public ResponseEntity<List<ResponseDTO>> buscarTodosHospedesForaDoHotel() {
        return ResponseEntity.ok(checkoutService.buscarTodosHospedesForaHotel());
    }
}
