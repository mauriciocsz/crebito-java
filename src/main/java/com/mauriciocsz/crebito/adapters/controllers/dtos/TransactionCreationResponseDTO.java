package com.mauriciocsz.crebito.adapters.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauriciocsz.crebito.domain.entities.User;

public class TransactionCreationResponseDTO {
    @JsonProperty
    private Long limite;

    @JsonProperty
    private Long saldo;

    public TransactionCreationResponseDTO(Long limite, Long saldo) {
        this.limite = limite;
        this.saldo = saldo;
    }

    public static TransactionCreationResponseDTO fromDomain(User user) {
        return new TransactionCreationResponseDTO(
            user.limit(),
            user.balance()
        );
    }
}
