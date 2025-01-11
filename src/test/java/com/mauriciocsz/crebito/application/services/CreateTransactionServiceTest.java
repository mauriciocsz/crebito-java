package com.mauriciocsz.crebito.application.services;

import com.mauriciocsz.crebito.application.exceptions.InvalidUserTransactionException;
import com.mauriciocsz.crebito.application.exceptions.UserNotFoundException;
import com.mauriciocsz.crebito.application.ports.UserTransactionExecuting;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.TransactionType;
import com.mauriciocsz.crebito.domain.entities.UserBalance;
import com.mauriciocsz.crebito.domain.usecases.UserBalanceValidationUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionServiceTest {
    @InjectMocks
    private CreateTransactionService service;

    @Mock
    private UserTransactionExecuting userTransactionExecuting;

    @Mock
    private UserBalanceValidationUseCase userBalanceValidationUseCase;

    @Test
    public void executesCredit() {
        String userId = "uid";
        Transaction request = mock(Transaction.class);

        when(request.getType()).thenReturn(TransactionType.CREDIT);

        UserBalance expectedBalance = mock(UserBalance.class);
        when(userTransactionExecuting.executeCredit(any(), any())).thenReturn(expectedBalance);

        UserBalance result = service.execute(userId, request);

        assertEquals(expectedBalance, result);
        verify(userTransactionExecuting).executeCredit(userId, request);
    }

    @Test
    public void executeCreditForUnknownUserThrows() {
        String userId = "uid";
        Transaction request = mock(Transaction.class);

        when(request.getType()).thenReturn(TransactionType.CREDIT);
        when(userTransactionExecuting.executeCredit(any(), any())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> service.execute(userId, request));
    }

    @Test
    public void executeDebitSuccessfully() {
        String userId = "uid";
        Transaction request = mock(Transaction.class);

        when(request.getType()).thenReturn(TransactionType.DEBIT);

        UserBalance expectedBalance = mock(UserBalance.class);
        when(userTransactionExecuting.executeDebit(any(), any())).thenReturn(expectedBalance);
        when(userBalanceValidationUseCase.isValid(any())).thenReturn(true);

        UserBalance result = service.execute(userId, request);

        assertEquals(expectedBalance, result);
        verify(userTransactionExecuting).executeDebit(userId, request);
    }

    @Test
    public void executeForDebitIsInvalidThrows() {
        String userId = "uid";
        Transaction request = mock(Transaction.class);

        when(request.getType()).thenReturn(TransactionType.DEBIT);

        UserBalance expectedBalance = mock(UserBalance.class);
        when(userTransactionExecuting.executeDebit(any(), any())).thenReturn(expectedBalance);
        when(userBalanceValidationUseCase.isValid(expectedBalance)).thenReturn(false);

        assertThrows(InvalidUserTransactionException.class, () -> service.execute(userId, request));
    }

    @Test
    public void executeDebitForUnknownUserThrows() {
        String userId = "uid";
        Transaction request = mock(Transaction.class);

        when(request.getType()).thenReturn(TransactionType.DEBIT);
        when(userTransactionExecuting.executeDebit(any(), any())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> service.execute(userId, request));
    }
}