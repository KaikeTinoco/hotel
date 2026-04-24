package com.desafio.hotel.services;

import com.desafio.hotel.dto.guest.GuestDto;
import com.desafio.hotel.entity.guest.Guest;
import com.desafio.hotel.exceptions.BadRequestException;
import com.desafio.hotel.exceptions.GuestNotFoundException;
import com.desafio.hotel.repositories.GuestRepository;
import com.desafio.hotel.specification.GuestSpecification;
import com.desafio.hotel.specification.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pela gestão de hóspedes.
 *
 * <p>Esta classe fornece operações para cadastrar, buscar, deletar e filtrar hóspedes
 * do hotel. Implementa funcionalidades de busca com critérios múltiplos.</p>
 *
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Cadastrar novos hóspedes no sistema</li>
 *   <li>Buscar hóspedes por diversos critérios (nome, documento, telefone)</li>
 *   <li>Deletar hóspedes do sistema</li>
 *   <li>Listar hóspedes que estão dentro ou fora do hotel</li>
 *   <li>Normalização de dados (remoção de caracteres especiais)</li>
 * </ul>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Service
public class GuestService {

    private final GuestRepository repository;

    @Autowired
    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    /**
     * Cadastra um novo hóspede no sistema.
     *
     * <p>Normaliza o nome removendo espaços e convertendo para minúsculas,
     * e remove caracteres especiais de documentos e telefones.</p>
     *
     * @param dto objeto contendo os dados do hóspede a cadastrar
     * @return o hóspede cadastrado
     * @throws BadRequestException se houver erro ao salvar o hóspede
     */
    public Guest cadastrarHospede(GuestDto dto){
        try {
            Guest guest = new Guest();
            guest.setNome(dto.getNome().toLowerCase().replace(" ","_"));
            guest.setDocumento(excluirTracosePontos(dto.getDocumento()));
            guest.setTelefone(excluirTracosePontos(dto.getTelefone()));
            guest.setDentroHotel(true);
            salvarGuest(guest);
            return guest;
        } catch (BadRequestException e) {
            throw new BadRequestException("Erro ao tentar salvar o hóspede, contate o  suporte");
        }

    }

    /**
     * Busca hóspedes aplicando filtros opcionais.
     *
     * <p>Permite filtrar por nome, documento e/ou telefone. Se nenhum filtro
     * for fornecido, retorna todos os hóspedes.</p>
     *
     * @param nome nome do hóspede (opcional)
     * @param documento documento do hóspede (opcional)
     * @param telefone telefone do hóspede (opcional)
     * @return lista de hóspedes que correspondem aos critérios
     */
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

    /**
     * Cria uma especificação para buscar hóspedes por nome.
     *
     * @param name nome a ser buscado (usa busca parcial)
     * @return GuestSpecification para busca por nome
     */
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

    /**
     * Cria uma especificação para buscar hóspedes por documento.
     *
     * @param document documento a ser buscado (usa busca parcial)
     * @return GuestSpecification para busca por documento
     */
    private GuestSpecification getGuestSpecifcationDocument(String document){
        GuestSpecification guestSpecification;
        guestSpecification=GuestSpecification.builder()
                .criteria(SearchCriteria.builder()
                        .key("documento")
                        .value(document)
                        .operation("%")
                        .build())
                .build();
        return guestSpecification;
    }

    /**
     * Cria uma especificação para buscar hóspedes por telefone.
     *
     * @param telefone telefone a ser buscado (usa busca parcial)
     * @return GuestSpecification para busca por telefone
     */
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

    /**
     * Valida se pelo menos um parâmetro de filtro foi fornecido.
     *
     * @param nome filtro por nome
     * @param documento filtro por documento
     * @param telefone filtro por telefone
     * @return true se pelo menos um parâmetro não é nulo
     */
    private boolean validarParametros(String nome, String documento, String telefone){
        return !(nome == null && documento == null && telefone == null);
    }

    /**
     * Deleta um hóspede do sistema pelo seu ID.
     *
     * @param id identificador único do hóspede a deletar
     * @return mensagem confirmando a exclusão do hóspede
     * @throws BadRequestException se o ID for nulo
     * @throws GuestNotFoundException se o hóspede não for encontrado
     */
    public String deletarGuestById(Long id){
        if (id == null){
            throw new BadRequestException("Id vazio, por favor envie um id válido");
        }
        try {
            Guest guest = repository.findById(id)
                    .orElseThrow(() -> new GuestNotFoundException("não existe um hospede com esse Id"));
            repository.delete(guest);
            return "Hóspede deletado com sucesso!";
        } catch (BadRequestException e){
            throw new BadRequestException("erro ao deletar hóspede, contate o suporte");
        }

    }

    /**
     * Salva um hóspede no repositório.
     *
     * @param guest hóspede a ser salvo
     */
    private void salvarGuest(Guest guest){
        repository.save(guest);
    }

    /**
     * Busca um hóspede por ID.
     *
     * @param guestId identificador único do hóspede
     * @return o hóspede encontrado
     * @throws BadRequestException se o ID for nulo
     * @throws GuestNotFoundException se o hóspede não for encontrado
     */
    public Guest findById(Long guestId) {
        if(guestId == null){
            throw new BadRequestException("Id vazio, por favor envie um id válido");
        }
        try {
            return repository.findById(guestId)
                    .orElseThrow(() -> new GuestNotFoundException("Hospede não encontrado, Id inválido"));
        }catch (BadRequestException e){
            throw new BadRequestException("erro ao buscar o hóspede, contate o suporte");
        }
    }

    /**
     * Remove caracteres especiais de uma string.
     *
     * <p>Remove espaços em branco, pontos, barras, hífens e parênteses.</p>
     *
     * @param s string a ser processada
     * @return string sem caracteres especiais
     */
    private String excluirTracosePontos(String s){
        s = s.replace( " " , ""); //tira espaço em branco
        s = s.replace( "." , ""); //tira ponto
        s = s.replace( "/" , ""); //tira barra
        s = s.replace( "-" , "");//tira hífen
        s = s.replace("(", "");
        s = s.replace(")","");
        return s;
    }

    /**
     * Busca todos os hóspedes cadastrados no sistema.
     *
     * @return lista com todos os hóspedes
     */
    public List<Guest> buscarTodosHospedes() {
        return repository.findAll();
    }

    /**
     * Busca hóspedes de acordo com sua posição no hotel.
     *
     * @param dentroHotel true para buscar hóspedes dentro do hotel, false para fora
     * @return lista de hóspedes de acordo com o critério
     * @throws BadRequestException se nenhum hóspede for encontrado
     */
    public List<Guest> buscarHospedeDentroOuForaHotel(boolean dentroHotel){
        String mensagemErro = dentroHotel?"Não há pessoas dentro do hotel!":"Não há pessoas fora do hotel!";
        return repository.findByDentroHotel(dentroHotel)
                    .orElseThrow(() -> new BadRequestException(mensagemErro));

    }


}
