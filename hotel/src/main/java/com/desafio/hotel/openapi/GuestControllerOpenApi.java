package com.desafio.hotel.openapi;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GuestControllerOpenApi {
     ResponseEntity<Guest> cadastrarHospede(@RequestBody GuestDto dto);
     ResponseEntity<List<Guest>> buscarTodosHospedes();
     ResponseEntity<List<Guest>> filtroDeBusca(@RequestParam(required = false) String nome,
                                                     @RequestParam(required = false) String documento,
                                                     @RequestParam(required = false) String telefone);
     ResponseEntity<String> deletarHospede(@RequestParam Long id);
}
