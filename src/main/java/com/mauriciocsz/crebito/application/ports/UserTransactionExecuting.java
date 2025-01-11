package com.mauriciocsz.crebito.application.ports;

import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.UserBalance;

public interface UserTransactionExecuting {
    UserBalance executeCredit(final String userID, final Transaction newTransaction);
    UserBalance executeDebit(final String userID, final Transaction newTransaction);
}
