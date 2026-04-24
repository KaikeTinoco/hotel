package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para persistência de dados de check-ins.
 *
 * <p>Fornece operações CRUD e consultas customizadas para a entidade Checkin.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    /**
     * Busca um check-in pelo seu ID.
     *
     * @param id identificador único do check-in
     * @return Optional contendo o check-in se encontrado
     */
    Optional<Checkin> findById(Long id);

    /**
     * Busca todos os check-ins associados a um hóspede específico.
     *
     * @param guestId identificador do hóspede
     * @return Optional com lista de check-ins do hóspede
     */
    Optional<List<Checkin>> findByGuestId(Long guestId);

    /**
     * Busca todos os check-ins filtrados pelo adicional de veículo.
     *
     * @param adicionalVeiculo true para buscar check-ins com veículo, false caso contrário
     * @return Optional com lista de check-ins filtrados
     */
    Optional<List<Checkin>> findByAdicionalVeiculo(boolean adicionalVeiculo);

}
