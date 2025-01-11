package com.mauriciocsz.crebito.application.services;

import com.mauriciocsz.crebito.application.exceptions.UserNotFoundException;
import com.mauriciocsz.crebito.application.ports.FindUser;
import com.mauriciocsz.crebito.application.ports.RetrieveTransactions;
import com.mauriciocsz.crebito.domain.entities.BankStatement;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class BuildBankStatementService {

    private final FindUser findUser;

    private final RetrieveTransactions retrieveTransactions;

    public BuildBankStatementService(final FindUser findUser, final RetrieveTransactions retrieveTransactions) {
        this.findUser = findUser;
        this.retrieveTransactions = retrieveTransactions;
    }

    @Transactional
    public BankStatement forUser(final String userId) {
        User user = getUserOrThrow(userId);
        List<Transaction> transactions = retrieveTransactions.getLatest10FromUser(userId);

        return new BankStatement(user, transactions);
    }

    private User getUserOrThrow(final String userId) {
        return findUser.byId(userId).orElseThrow(
            () -> new UserNotFoundException(userId)
        );
    }
}
