package com.desafio.hotel.entity.reservation;

public enum StatusReserva {
    PENDENTE("pendente"),
    UTILIZADA("utilizada"),
    CANCELADA("cancelada"),
    EXPIRADA("expirada");

    private final String status;

    StatusReserva(String status) {
        this.status = status;
    }
}
