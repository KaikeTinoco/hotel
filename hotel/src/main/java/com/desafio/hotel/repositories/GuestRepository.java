package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.guest.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para persistência de dados de hóspedes.
 *
 * <p>Fornece operações CRUD e suporta buscas com especificações (Specification)
 * para realizar consultas complexas e dinâmicas.</p>
 *
 * @author Desafio Hotel
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>, JpaSpecificationExecutor<Guest> {
    /**
     * Busca um hóspede pelo seu ID.
     *
     * @param id identificador único do hóspede
     * @return Optional contendo o hóspede se encontrado
     */
    Optional<Guest> findById(Long id);

    /**
     * Busca hóspedes de acordo com sua posição no hotel.
     *
     * @param dentroHotel true para buscar hóspedes dentro do hotel, false para fora
     * @return Optional com lista de hóspedes de acordo com o filtro
     */
    Optional<List<Guest>> findByDentroHotel(boolean dentroHotel);
}
