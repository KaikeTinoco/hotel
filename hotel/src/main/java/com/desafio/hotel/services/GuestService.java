package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.repositories.GuestRepository;
import com.desafio.hotel.specification.GuestSpecification;
import com.desafio.hotel.specification.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    @Autowired
    private GuestRepository repository;

    public Guest cadastrarHospede(GuestDto dto){
        Guest guest = new Guest();
        guest.setNome(dto.getNome());
        guest.setDocumento(excluirTracosePontos(dto.getDocumento()));
        guest.setTelefone(excluirTracosePontos(dto.getTelefone()));
        guest.setDentroHotel(true);
        salvarGuest(guest);
        return guest;
    }

    public List<Guest> filtroDeBusca(String nome, String documento, String telefone){
        if(validarParametros(nome, documento, telefone)){
            GuestSpecification nomeSpecification = null;
            GuestSpecification documentoSpecification = null;
            GuestSpecification telefoneSpecification = null;

            if (nome != null){
                nomeSpecification = getGuestSpecifcationName(nome);
            }
            if(documento!= null){
                documentoSpecification = getGuestSpecifcationDocument(documento);
            }
            if(telefone!= null){
                telefoneSpecification = getGuestSpecifcationTelefone(telefone);
            }

            Specification<Guest>guestSpecification = Specification.where(nomeSpecification)
                    .and(documentoSpecification)
                    .and(telefoneSpecification);
            return repository.findAll(guestSpecification);
        }
        return buscarTodosHospedes();

    }

    private GuestSpecification getGuestSpecifcationName(String name){
        GuestSpecification guestSpecification;
        guestSpecification=GuestSpecification.builder()
                .criteria(SearchCriteria.builder()
                        .key("nome")
                        .value(name)
                        .operation("%")
                        .build())
                .build();
        return guestSpecification;
    }

    private GuestSpecification getGuestSpecifcationDocument(String document){
        GuestSpecification guestSpecification;
        guestSpecification=GuestSpecification.builder()
                .criteria(SearchCriteria.builder()
                        .key("nome")
                        .value(document)
                        .operation("%")
                        .build())
                .build();
        return guestSpecification;
    }

    private GuestSpecification getGuestSpecifcationTelefone(String telefone){
        GuestSpecification guestSpecification;
        guestSpecification=GuestSpecification.builder()
                .criteria(SearchCriteria.builder()
                        .key("telefone")
                        .value(telefone)
                        .operation("%")
                        .build())
                .build();
        return guestSpecification;
    }

    private boolean validarParametros(String nome, String documento, String telefone){
        return !(nome == null && documento == null && telefone == null);
    }

    public String deletarGuestById(Long id) throws Exception {
        Guest guest = repository.findById(id).orElseThrow(() -> new Exception("não existe um hospede com esse Id"));
        repository.delete(guest);
        return "Hóspede deletado com sucesso!";
    }

    private void salvarGuest(Guest guest){
        repository.save(guest);
    }

    public Guest findById(Long guestId) throws Exception {
        return repository.findById(guestId).orElseThrow(() -> new Exception(""));
    }

    private String excluirTracosePontos(String s){
        s = s.replace( " " , ""); //tira espaço em branco
        s = s.replace( "." , ""); //tira ponto
        s = s.replace( "/" , ""); //tira barra
        s = s.replace( "-" , "");//tira hífen
        s = s.replace("(", "");
        s = s.replace(")","");
        return s;
    }

    public List<Guest> buscarTodosHospedes() {
        return repository.findAll();
    }

    public List<Guest> buscarHospedesDentroHotel() throws Exception {
        return repository.findByDentroHotel(true).orElseThrow(() -> new Exception("não tem ninguem dentro do hotel!"));
    }
}
