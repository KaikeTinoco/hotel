package com.desafio.hotel.resource;

import com.desafio.hotel.dto.checkin.CheckinCreateDto;
import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.openapi.CheckinControllerOpenApi;
import com.desafio.hotel.services.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gerenciamento de check-ins.
 *
 * <p>Fornece endpoints para criar e deletar check-ins de hóspedes.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/checkin")
public class CheckinController implements CheckinControllerOpenApi {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    /**
     * Cria um novo check-in para um hóspede.
     *
     * @param dto dados do check-in a criar
     * @return ResponseEntity contendo o check-in criado
     */
    @PostMapping(path = "/cadastrarCheckin")
    public ResponseEntity<Checkin> criarCheckin(@RequestBody CheckinCreateDto dto)  {
        return ResponseEntity.ok(checkinService.criarCheckin(dto));
    }

    /**
     * Deleta um check-in existente.
     *
     * @param id identificador do check-in a deletar
     * @return ResponseEntity com mensagem de sucesso
     */
    @DeleteMapping(path = "/deletarCheckin")
    public ResponseEntity<String> deletarCheckin(@RequestParam Long id)  {
        return ResponseEntity.ok(checkinService.deletarCheckin(id));
    }

}
