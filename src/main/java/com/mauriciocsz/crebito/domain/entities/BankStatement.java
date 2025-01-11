package com.mauriciocsz.crebito.domain.entities;

import java.time.ZonedDateTime;
import java.util.List;

public class BankStatement {
    private final User user;
    private final List<Transaction> transactions;
    private final ZonedDateTime date;

    public User getUser() {
        return user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public BankStatement(final User user, final List<Transaction> transactions) {
        this.user = user;
        this.transactions = transactions;
        this.date = ZonedDateTime.now();
    }
}
