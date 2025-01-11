package com.mauriciocsz.crebito.adapters.repositories.models;

import com.mauriciocsz.crebito.domain.entities.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Collection;

@Entity
@Table(name = "\"user\"")
public class UserModel {

    @Id
    @Column(name = "id")
    public String userId;

    @Column(name = "credit_limit")
    public Long limit;

    @Column
    public Long balance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<TransactionModel> transactions;

    public UserModel() {}

    public UserModel(String identifier, Long limit, Long balance) {
        this.userId = identifier;
        this.limit = limit;
        this.balance = balance;
    }

    public User toDomain() {
        return new User(
            this.userId,
            this.limit,
            this.balance
        );
    }
}
