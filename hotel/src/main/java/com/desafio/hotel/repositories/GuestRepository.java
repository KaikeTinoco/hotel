package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.guest.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>, JpaSpecificationExecutor<Guest> {
     Optional<Guest> findById(Long id);
     Optional<List<Guest>> findByDentroHotel(boolean dentroHotel);
}
