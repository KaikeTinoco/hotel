package com.desafio.hotel.resource;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path = "/guests")
public class GuestController {
    @Autowired
    private GuestService guestService;

    @PostMapping(path = "/cadastro")
    public ResponseEntity<Guest> cadastrarHospede(@RequestBody GuestDto dto){
        return ResponseEntity.ok(guestService.cadastrarHospede(dto));
    }

    @GetMapping(path = "/buscaTodosHospedes")
    public ResponseEntity<List<Guest>> buscarTodosHospedes(){
        return ResponseEntity.ok(guestService.buscarTodosHospedes());
    }


    @GetMapping(path = "/buscaHospedes")
    public ResponseEntity<List<Guest>> filtroDeBusca(@RequestParam(required = false) String nome,
                                                     @RequestParam(required = false) String documento,
                                                     @RequestParam(required = false) String telefone){
        return ResponseEntity.ok(guestService.filtroDeBusca(nome, documento, telefone));
    }

    @DeleteMapping(path = "/deletarHospedes")
    public ResponseEntity<String> deletarHospede(@RequestParam Long id) throws Exception {
        return ResponseEntity.ok(guestService.deletarGuestById(id));
    }


}
