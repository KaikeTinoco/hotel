package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para persistência de dados de check-outs.
 *
 * <p>Fornece operações CRUD e consultas customizadas para a entidade Checkout.</p>
 *
 * @author Kaike Tinoco
 * @version 1.0
 * @since 1.0
 */
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    /**
     * Busca um check-out pelo seu ID.
     *
     * @param id identificador único do check-out
     * @return Optional contendo o check-out se encontrado
     */
    Optional<Checkout> findById(Long id);

    /**
     * Busca todos os check-outs associados a um check-in específico.
     *
     * @param checkinId identificador do check-in
     * @return Optional com lista de check-outs do check-in
     */
    Optional<List<Checkout>> findByCheckinId(Long checkinId);

}
