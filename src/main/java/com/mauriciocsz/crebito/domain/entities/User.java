package com.mauriciocsz.crebito.domain.entities;

import java.util.Objects;

public final class User {
    private final String identifier;
    private final Long limit;
    private Long balance;

    public User(final String identifier, final Long limit, final Long balance) {
        this.identifier = identifier;
        this.limit = limit;
        this.balance = balance;
    }

    public String identifier() {
        return identifier;
    }

    public Long limit() {
        return limit;
    }

    public Long balance() {
        return balance;
    }

    public void decreaseBalance(final long amount) {
        this.balance -= amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.identifier, that.identifier) &&
                Objects.equals(this.limit, that.limit) &&
                Objects.equals(this.balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, limit, balance);
    }

    @Override
    public String toString() {
        return "User[" +
                "identifier=" + identifier + ", " +
                "limit=" + limit + ", " +
                "balance=" + balance + ']';
    }

}
