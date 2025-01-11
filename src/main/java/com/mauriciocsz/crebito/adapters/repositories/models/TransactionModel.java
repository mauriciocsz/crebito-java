package com.mauriciocsz.crebito.adapters.repositories.models;

import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.TransactionType;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transaction")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    public Integer id;

    public TransactionModel() {
    }

    @Column
    public Long value;

    @Column
    public Character type;

    @Column
    public String description;

    @Column
    public Timestamp date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public UserModel user;

    public TransactionModel(Integer id, Long value, Character type, String description, Timestamp date, UserModel user) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
        this.user = user;
    }

    public TransactionModel(Long value, Character type, String description, Timestamp date, UserModel user) {
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
        this.user = user;
    }

    public Transaction toDomain() {
        return new Transaction(
            this.value,
            typeDigitToType(),
            this.description,
            ZonedDateTime.ofInstant(this.date.toInstant(), ZoneId.systemDefault())
        );
    }

    private TransactionType typeDigitToType() {
        return switch (this.type) {
            case 'd' -> TransactionType.DEBIT;
            case 'c' -> TransactionType.CREDIT;
            default -> null;
        };
    }

    public static char typeToTypeDigit(TransactionType type) {
        return switch (type) {
            case DEBIT -> 'd';
            case CREDIT -> 'c';
        };
    }
}
