package com.mauriciocsz.crebito.adapters.repositories;

import com.mauriciocsz.crebito.adapters.repositories.models.TransactionModel;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Repository
public class TransactionRepository {

    private final JdbcClient jdbcClient;

    private static final String LATEST_10_TRANSACTIONS_RETRIEVAL_QUERY =
        "SELECT * FROM transaction WHERE user_id = ? ORDER BY date DESC LIMIT 10;";

    public Set<TransactionModel> getLatest10FromUser(String userId) {
        return jdbcClient.sql(LATEST_10_TRANSACTIONS_RETRIEVAL_QUERY)
            .param(userId)
            .query(TransactionModel.class)
            .set();
    }

    private static final String INSERT_TRANSACTION_QUERY = """
        INSERT INTO "transaction"(value, type, description, date, user_id) VALUES(?, ?, ?, ?, ?);
    """;

    public void insertNewTransaction(Transaction newTransaction, String userID) {
        jdbcClient.sql(INSERT_TRANSACTION_QUERY)
            .param(newTransaction.getAmount())
            .param(TransactionModel.typeToTypeDigit(newTransaction.getType()))
            .param(newTransaction.getDescription())
            .param(Timestamp.from(Instant.now()))
            .param(userID)
            .update();
    }

    public TransactionRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
