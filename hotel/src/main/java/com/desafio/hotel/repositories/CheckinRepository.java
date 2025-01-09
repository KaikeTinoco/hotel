package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Optional<Checkin> findById(Long id);
    Optional<Checkin> findByAdicionalVeiculo(boolean adicionalVeiculo);

}
