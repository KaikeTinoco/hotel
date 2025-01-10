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
    @Autowired
    private CheckinService checkinService;

    @PostMapping(path = "/cadastrarCheckin")
    public ResponseEntity<Checkin> criarCheckin(@RequestBody CheckinCreateDto dto) throws Exception {
        return ResponseEntity.ok(checkinService.criarCheckin(dto));
    }

    @DeleteMapping(path = "/deletarCheckin")
    public ResponseEntity<String> deletarCheckin(@RequestParam Long id) throws Exception {
        return ResponseEntity.ok(checkinService.deletarCheckin(id));
    }
}
