package com.mauriciocsz.crebito.domain.entities;

import java.time.ZonedDateTime;

public class Transaction {
    private final Long amount;
    private final TransactionType type;
    private final String description;
    private ZonedDateTime date;

    public Transaction(final Long amount, final TransactionType type, final String description, final ZonedDateTime date) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public Transaction(final Long amount, final TransactionType type, final String description) {
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Long getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

