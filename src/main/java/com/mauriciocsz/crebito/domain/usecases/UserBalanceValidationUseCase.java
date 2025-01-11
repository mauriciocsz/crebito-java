package com.mauriciocsz.crebito.domain.usecases;

import com.mauriciocsz.crebito.domain.entities.UserBalance;
import org.springframework.stereotype.Component;

@Component
public class UserBalanceValidationUseCase {
    public boolean isValid(UserBalance userBalance) {
        return userBalance.balance()  >= -userBalance.limit();
    }
}