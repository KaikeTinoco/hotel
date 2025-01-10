package com.desafio.hotel.repositories;

import com.desafio.hotel.entity.checkin.Checkin;
import com.desafio.hotel.entity.checkout.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Optional<Checkout> findById(Long id);
    Optional<List<Checkin>> findByGuestId(Long guestId);
    Optional<Checkout> findByAdicionalVeiculo(boolean adicionalVeiculo);
}
