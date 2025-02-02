package com.desafio.hotel.openapi;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.entity.checkin.Checkin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CheckinControllerOpenApi {
     ResponseEntity<Checkin> criarCheckin(@RequestBody CheckinCreateDto dto);
     ResponseEntity<String> deletarCheckin(@RequestParam Long id);

}
