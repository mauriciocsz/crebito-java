package com.mauriciocsz.crebito.adapters.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauriciocsz.crebito.adapters.controllers.dtos.TransactionDTO;
import com.mauriciocsz.crebito.adapters.repositories.models.UserModel;
import com.mauriciocsz.crebito.application.exceptions.InvalidUserTransactionException;
import com.mauriciocsz.crebito.application.exceptions.UserNotFoundException;
import com.mauriciocsz.crebito.application.services.BuildBankStatementService;
import com.mauriciocsz.crebito.application.services.CreateTransactionService;
import com.mauriciocsz.crebito.domain.entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {UsersController.class})
class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildBankStatementService buildBankStatementService;

    @MockBean
    private CreateTransactionService createTransactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBankStatement() throws Exception{
        String userID = "uid";

        BankStatement bankStatement = new BankStatement(
            new User(
                userID,
                9999L,
                5678L
            ),
            List.of(
                new Transaction(
                    1234L,
                    TransactionType.CREDIT,
                    "whatever"
                )
            )
        );

        when(buildBankStatementService.forUser(userID)).thenReturn(bankStatement);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/clientes/{id}/extrato", userID)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(200))
            .andExpect(jsonPath("saldo.total").value(5678L))
            .andExpect(jsonPath("saldo.limite").value(9999L))
            .andExpect(jsonPath("$.ultimas_transacoes[0].valor").value(1234L))
            .andExpect(jsonPath("$.ultimas_transacoes[0].descricao").value("whatever"));

    }

    @Test
    void getBankStatementDoesNotFindUser() throws Exception{
        String userID = "uid";

        when(buildBankStatementService.forUser(userID)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/clientes/{id}/extrato", userID)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(404));
    }


    @Test
    void createTransaction() throws Exception{
        String userID = "uid";

        Instant instant = Instant.ofEpochSecond(32432432432L);

        TransactionDTO requestDTO = mock(TransactionDTO.class);

        ReflectionTestUtils.setField(requestDTO, "valor", BigDecimal.valueOf(1234L));
        ReflectionTestUtils.setField(requestDTO, "tipo", "c");
        ReflectionTestUtils.setField(requestDTO, "descricao", "aDesc");

        UserBalance resultingBalance = mock(UserBalance.class);
        when(resultingBalance.limit()).thenReturn(9999L);
        when(resultingBalance.balance()).thenReturn(5678L);

        when(createTransactionService.execute(anyString(), any(Transaction.class))).thenReturn(resultingBalance);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/clientes/{id}/transacoes", userID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO))
            ).andExpect(status().is(200))
            .andExpect(jsonPath("saldo").value(5678L))
            .andExpect(jsonPath("limite").value(9999L));
    }

    @Test
    void createTransactionDoesNotFindUser() throws Exception {
        String userID = "uid";

        Instant instant = Instant.ofEpochSecond(32432432432L);

        TransactionDTO requestDTO = mock(TransactionDTO.class);

        ReflectionTestUtils.setField(requestDTO, "valor", BigDecimal.valueOf(1234L));
        ReflectionTestUtils.setField(requestDTO, "tipo", "c");
        ReflectionTestUtils.setField(requestDTO, "descricao", "aDesc");

        when(createTransactionService.execute(anyString(), any(Transaction.class)))
            .thenThrow(UserNotFoundException.class);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/clientes/{id}/transacoes", userID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().is(404));
    }


    @Test
    void createTransactionIsInvalid() throws Exception {
        String userID = "uid";

        Instant instant = Instant.ofEpochSecond(32432432432L);

        TransactionDTO requestDTO = mock(TransactionDTO.class);

        ReflectionTestUtils.setField(requestDTO, "valor", BigDecimal.valueOf(1234L));
        ReflectionTestUtils.setField(requestDTO, "tipo", "d");
        ReflectionTestUtils.setField(requestDTO, "descricao", "aDesc");

        when(createTransactionService.execute(anyString(), any(Transaction.class)))
            .thenThrow(InvalidUserTransactionException.class);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/clientes/{id}/transacoes", userID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().is(422));
    }
}