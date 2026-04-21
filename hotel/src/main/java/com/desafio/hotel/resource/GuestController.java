package com.desafio.hotel.resource;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.openapi.GuestControllerOpenApi;
import com.desafio.hotel.services.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de hóspedes.
 *
 * <p>Fornece endpoints para cadastrar, buscar, deletar e filtrar hóspedes.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@RestController()
@RequestMapping(path = "/guests")
public class GuestController implements GuestControllerOpenApi {
    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Cadastra um novo hóspede no sistema.
     *
     * @param dto dados do novo hóspede
     * @return ResponseEntity com o hóspede cadastrado
     */
    @PostMapping(path = "/cadastro")
    public ResponseEntity<Guest> cadastrarHospede(@RequestBody GuestDto dto){
        return ResponseEntity.ok(guestService.cadastrarHospede(dto));
    }

    /**
     * Busca todos os hóspedes cadastrados.
     *
     * @return ResponseEntity com lista de todos os hóspedes
     */
    @GetMapping(path = "/buscaTodosHospedes")
    public ResponseEntity<List<Guest>> buscarTodosHospedes(){
        return ResponseEntity.ok(guestService.buscarTodosHospedes());
    }

    /**
     * Busca hóspedes aplicando filtros opcionais.
     *
     * @param nome filtro por nome (opcional)
     * @param documento filtro por documento (opcional)
     * @param telefone filtro por telefone (opcional)
     * @return ResponseEntity com lista de hóspedes que correspondem aos filtros
     */
    @GetMapping(path = "/buscaHospedes")
    public ResponseEntity<List<Guest>> filtroDeBusca(@RequestParam(required = false) String nome,
                                                     @RequestParam(required = false) String documento,
                                                     @RequestParam(required = false) String telefone){
        return ResponseEntity.ok(guestService.filtroDeBusca(nome, documento, telefone));
    }

    /**
     * Deleta um hóspede do sistema.
     *
     * @param id identificador do hóspede a deletar
     * @return ResponseEntity com mensagem de sucesso
     */
    @DeleteMapping(path = "/deletarHospedes")
    public ResponseEntity<String> deletarHospede(@RequestParam Long id)  {
        return ResponseEntity.ok(guestService.deletarGuestById(id));
    }

}
