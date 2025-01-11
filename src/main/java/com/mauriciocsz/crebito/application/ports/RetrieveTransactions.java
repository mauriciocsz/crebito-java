package com.mauriciocsz.crebito.application.ports;

import com.mauriciocsz.crebito.domain.entities.Transaction;

import java.util.List;

public interface RetrieveTransactions {
    List<Transaction> getLatest10FromUser(String userId);
}
