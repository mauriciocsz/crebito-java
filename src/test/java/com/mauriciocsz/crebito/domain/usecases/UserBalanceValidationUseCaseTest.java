package com.mauriciocsz.crebito.domain.usecases;

import com.mauriciocsz.crebito.domain.entities.UserBalance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserBalanceValidationUseCaseTest {

    @InjectMocks
    private UserBalanceValidationUseCase userBalanceValidationUseCase;

    @Test
    void balanceWithinLimits() {
        UserBalance balance = mock(UserBalance.class);

        long bigLimit = Long.MAX_VALUE;

        when(balance.limit()).thenReturn(bigLimit);
        when(balance.balance()).thenReturn(-10L);

        assertTrue(userBalanceValidationUseCase.isValid(balance));
    }

    @Test
    void balanceIsEqualToLimit() {
        UserBalance balance = mock(UserBalance.class);

        long bigLimit = Long.MAX_VALUE;

        when(balance.limit()).thenReturn(bigLimit);
        when(balance.balance()).thenReturn(-bigLimit);

        assertTrue(userBalanceValidationUseCase.isValid(balance));
    }


    @Test
    void balanceExceedsLimit() {
        UserBalance balance = mock(UserBalance.class);

        long bigLimit = Long.MAX_VALUE;

        when(balance.limit()).thenReturn(bigLimit);
        when(balance.balance()).thenReturn(-(bigLimit + 1));

        assertFalse(userBalanceValidationUseCase.isValid(balance));
    }
}