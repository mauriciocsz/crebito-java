package com.mauriciocsz.crebito.adapters.repositories;

import com.mauriciocsz.crebito.adapters.repositories.models.UserBalanceModel;
import com.mauriciocsz.crebito.adapters.repositories.models.UserModel;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    private static final String BALANCE_ALTERING_QUERY = """
        UPDATE "user" SET balance = balance + ?
            WHERE id = ?
            RETURNING credit_limit AS limit, balance;
    """;

    public UserBalanceModel alterBalanceForUser(final String userID, final long delta) {
        return jdbcClient.sql(BALANCE_ALTERING_QUERY)
            .param(delta)
            .param(userID)
            .query(UserBalanceModel.class)
            .optional().orElse(null);
    }

    private static final String FIND_USER_BY_ID_QUERY = """
        SELECT id AS userId, credit_limit AS limit, balance FROM "user" WHERE id = ?;
    """;

    public Optional<UserModel> findById(final String userID) {
        return jdbcClient.sql(FIND_USER_BY_ID_QUERY)
            .param(userID)
            .query(UserModel.class)
            .optional();
    }

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}

