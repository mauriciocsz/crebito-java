package com.mauriciocsz.crebito.adapters.controllers;

import com.mauriciocsz.crebito.adapters.controllers.dtos.BankStatementDTO;
import com.mauriciocsz.crebito.adapters.controllers.dtos.TransactionCreationResponseDTO;
import com.mauriciocsz.crebito.adapters.controllers.dtos.TransactionDTO;
import com.mauriciocsz.crebito.application.services.BuildBankStatementService;
import com.mauriciocsz.crebito.application.services.CreateTransactionService;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.UserBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class UsersController {

    @Autowired
    private BuildBankStatementService buildBankStatementService;

    @Autowired
    private CreateTransactionService createTransactionService;

    @GetMapping("{id}/extrato")
    @ResponseStatus(value = HttpStatus.OK)
    public BankStatementDTO getBankStatement(@PathVariable("id") String userId) {
        return BankStatementDTO.fromDomain(buildBankStatementService.forUser(userId));
    }

    @PostMapping("{id}/transacoes")
    @ResponseStatus(value = HttpStatus.OK)
    public TransactionCreationResponseDTO createTransaction(
            @PathVariable("id") String userId,
            @RequestBody @Validated TransactionDTO transactionDTO
    ) {
        Transaction transaction = transactionDTO.toDomain();
        UserBalance user = createTransactionService.execute(userId, transaction);
        return new TransactionCreationResponseDTO(user.limit(), user.balance());
    }
}
