package com.mauriciocsz.crebito.adapters.repositories.models;

import com.mauriciocsz.crebito.domain.entities.UserBalance;

public class UserBalanceModel {
    public Long limit;
    public Long balance;

    public UserBalanceModel(Long limit, Long balance) {
        this.limit = limit;
        this.balance = balance;
    }

    public UserBalance toDomain() {
        return new UserBalance(
            this.limit,
            this.balance
        );
    }
}