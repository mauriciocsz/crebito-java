package com.mauriciocsz.crebito.adapters.facades;

import com.mauriciocsz.crebito.adapters.repositories.TransactionRepository;
import com.mauriciocsz.crebito.adapters.repositories.UserRepository;
import com.mauriciocsz.crebito.adapters.repositories.models.TransactionModel;
import com.mauriciocsz.crebito.adapters.repositories.models.UserBalanceModel;
import com.mauriciocsz.crebito.application.ports.RetrieveTransactions;
import com.mauriciocsz.crebito.application.ports.UserTransactionExecuting;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.UserBalance;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class TransactionDatabaseFacade implements RetrieveTransactions, UserTransactionExecuting {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getLatest10FromUser(final String userId) {
        return transactionRepository.getLatest10FromUser(userId).stream().map(TransactionModel::toDomain).toList();
    }

    @Override
    @Transactional
    public UserBalance executeDebit(final String userID, final Transaction newTransaction) {
        return executeTransaction(userID, newTransaction, -newTransaction.getAmount());
    }

    @Override
    @Transactional
    public UserBalance executeCredit(final String userID, final Transaction newTransaction) {
        return executeTransaction(userID, newTransaction, newTransaction.getAmount());
    }

    private UserBalance executeTransaction(final String userID, final Transaction newTransaction, final long delta) {
        UserBalanceModel userBalance = userRepository.alterBalanceForUser(userID, delta);

        if (userBalance == null) {
            return null;
        } else {
            transactionRepository.insertNewTransaction(newTransaction, userID);
            return userBalance.toDomain();
        }
    }

    public TransactionDatabaseFacade(
        final TransactionRepository transactionRepository,
        UserRepository userRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }
}
