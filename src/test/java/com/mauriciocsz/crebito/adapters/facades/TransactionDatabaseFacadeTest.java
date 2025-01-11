package com.mauriciocsz.crebito.adapters.facades;

import com.mauriciocsz.crebito.adapters.repositories.TransactionRepository;
import com.mauriciocsz.crebito.adapters.repositories.UserRepository;
import com.mauriciocsz.crebito.adapters.repositories.models.TransactionModel;
import com.mauriciocsz.crebito.adapters.repositories.models.UserBalanceModel;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.UserBalance;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDatabaseFacadeTest {

    @InjectMocks
    private TransactionDatabaseFacade facade;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private final Instant instant = Instant.ofEpochSecond(2323243243L);

    @Test
    void getLatest10TransactionsForUser() {
        String userID = "uid";

        Set<TransactionModel> models = Set.of(
            new TransactionModel(
                1000L,
                'c',
                "credito de horas bonus",
                Timestamp.from(instant),
                null
            ),
            new TransactionModel(
                3200L,
                'd',
                "comida",
                Timestamp.from(instant.minusMillis(10000)),
                null
            )
        );

        when(transactionRepository.getLatest10FromUser(userID)).thenReturn(models);

        List<Transaction> result = facade.getLatest10FromUser(userID);
        List<Transaction> expectedResult = models.stream().map(TransactionModel::toDomain).toList();

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void executesDebit() {
        String userID = "uid";
        Transaction transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(1234L);

        UserBalance expectedUserBalance = mock(UserBalance.class);
        UserBalanceModel model = mock(UserBalanceModel.class);
        when(model.toDomain()).thenReturn(expectedUserBalance);

        when(userRepository.alterBalanceForUser(userID, -transaction.getAmount())).thenReturn(model);

        UserBalance result =  facade.executeDebit(userID, transaction);

        assertEquals(expectedUserBalance, result);

        verify(userRepository).alterBalanceForUser(userID, -transaction.getAmount());
        verify(transactionRepository).insertNewTransaction(transaction, userID);
    }

    @Test
    void executesCredit() {
        String userID = "uid";
        Transaction transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(1234L);

        UserBalance expectedUserBalance = mock(UserBalance.class);
        UserBalanceModel model = mock(UserBalanceModel.class);
        when(model.toDomain()).thenReturn(expectedUserBalance);

        when(userRepository.alterBalanceForUser(anyString(), anyLong())).thenReturn(model);

        UserBalance result =  facade.executeCredit(userID, transaction);

        assertEquals(expectedUserBalance, result);

        verify(userRepository).alterBalanceForUser(userID, transaction.getAmount());
        verify(transactionRepository).insertNewTransaction(transaction, userID);
    }

    @Test
    void returnNullWhenExecutingTransactionForUnknownUser() {
        String userID = "uid";
        Transaction transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(1234L);

        when(userRepository.alterBalanceForUser(userID, transaction.getAmount())).thenReturn(null);

        UserBalance result = facade.executeCredit(userID, transaction);

        assertNull(result);

        verify(transactionRepository, never()).insertNewTransaction(any(Transaction.class), anyString());
    }
}