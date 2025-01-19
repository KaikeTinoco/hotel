package com.desafio.hotel.resource;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.services.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/checkin")
public class CheckinController {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }


    @PostMapping(path = "/cadastrarCheckin")
    public ResponseEntity<Checkin> criarCheckin(@RequestBody CheckinCreateDto dto)  {
        return ResponseEntity.ok(checkinService.criarCheckin(dto));
    }

    @DeleteMapping(path = "/deletarCheckin")
    public ResponseEntity<String> deletarCheckin(@RequestParam Long id)  {
        return ResponseEntity.ok(checkinService.deletarCheckin(id));
    }


}
