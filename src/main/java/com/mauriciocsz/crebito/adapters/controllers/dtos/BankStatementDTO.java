package com.mauriciocsz.crebito.adapters.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauriciocsz.crebito.domain.entities.BankStatement;

import java.util.List;

public class BankStatementDTO {
    @JsonProperty(value = "saldo")
    private BalanceDTO balance;
    @JsonProperty("ultimas_transacoes")
    private List<TransactionDTO> transactions;

    private BankStatementDTO(BalanceDTO balance, List<TransactionDTO> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }

    public static BankStatementDTO fromDomain(BankStatement domain) {
        return new BankStatementDTO(
            BalanceDTO.fromDomain(domain.getUser(), domain.getDate()),
            domain.getTransactions().stream().map( (TransactionDTO::fromDomain)).toList()
        );
    }
}

