package com.mauriciocsz.crebito.application.services;

import com.mauriciocsz.crebito.application.exceptions.InvalidUserTransactionException;
import com.mauriciocsz.crebito.application.exceptions.UserNotFoundException;
import com.mauriciocsz.crebito.application.ports.UserTransactionExecuting;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.TransactionType;
import com.mauriciocsz.crebito.domain.entities.UserBalance;
import com.mauriciocsz.crebito.domain.usecases.UserBalanceValidationUseCase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreateTransactionService {
    private final UserTransactionExecuting userTransactionExecuting;
    private final UserBalanceValidationUseCase userBalanceValidationUseCase;

    @Transactional
    public UserBalance execute(final String userId, final Transaction request) {
        return switch (request.getType()) {
            case CREDIT -> executeForCredit(userId, request);
            case DEBIT -> executeForDebit(userId, request);
        };
    }

    private UserBalance executeForCredit(final String userId, final Transaction transactionRequest) {
        UserBalance resultingBalance =  userTransactionExecuting.executeCredit(userId, transactionRequest);

        if (resultingBalance == null) {
            throw new UserNotFoundException(userId);
        } else {
            return resultingBalance;
        }
    }

    private UserBalance executeForDebit(final String userId, final Transaction transactionRequest) {
        UserBalance resultingBalance =  userTransactionExecuting.executeDebit(userId, transactionRequest);

        if (resultingBalance == null) {
            throw new UserNotFoundException(userId);
        } else if (!userBalanceValidationUseCase.isValid(resultingBalance)) {
            throw new InvalidUserTransactionException("Transaction could not be performed because it is invalid.");
        } else {
            return resultingBalance;
        }
    }

    public CreateTransactionService(UserTransactionExecuting userTransactionExecuting, UserBalanceValidationUseCase userBalanceValidationUseCase) {
        this.userTransactionExecuting = userTransactionExecuting;
        this.userBalanceValidationUseCase = userBalanceValidationUseCase;
    }
}
