package com.mauriciocsz.crebito.application.services;

import com.mauriciocsz.crebito.application.exceptions.UserNotFoundException;
import com.mauriciocsz.crebito.application.ports.FindUser;
import com.mauriciocsz.crebito.application.ports.RetrieveTransactions;
import com.mauriciocsz.crebito.domain.entities.BankStatement;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildBankStatementServiceTest {

    @InjectMocks
    private BuildBankStatementService service;

    @Mock
    private FindUser findUser;

    @Mock
    private RetrieveTransactions retrieveTransactions;

    @Test
    public void buildsSuccessfully() {
        User user = mock(User.class);
        List<Transaction> transactions = mock(List.class);

        String userID = "UID";

        when(findUser.byId(userID)).thenReturn(Optional.of(user));
        when(retrieveTransactions.getLatest10FromUser(userID)).thenReturn(transactions);

        BankStatement result = service.forUser(userID);

        assertEquals(user, result.getUser());
        assertEquals(transactions, result.getTransactions());
    }

    @Test
    public void worksSuccessfullyWithNoTransactions() {
        User user = mock(User.class);
        List<Transaction> emptyTransactions = Collections.emptyList();

        String userID = "UID";

        when(findUser.byId(userID)).thenReturn(Optional.of(user));
        when(retrieveTransactions.getLatest10FromUser(userID)).thenReturn(emptyTransactions);

        BankStatement result = service.forUser(userID);

        assertEquals(user, result.getUser());
        assertEquals(emptyTransactions, result.getTransactions());
    }


    @Test
    public void throwsWhenUserIsNotFound() {
        String userID = "UID";

        when(findUser.byId(userID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.forUser(userID));
    }
}