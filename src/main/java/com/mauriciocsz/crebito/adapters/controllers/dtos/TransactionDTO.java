package com.mauriciocsz.crebito.adapters.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauriciocsz.crebito.domain.entities.Transaction;
import com.mauriciocsz.crebito.domain.entities.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionDTO {
    @JsonProperty
    @NotNull
    @Positive
    @Digits(integer = Long.SIZE, fraction = 0)
    public BigDecimal valor;
    @JsonProperty
    @NotNull
    @Pattern(regexp = "^[dc]$", message = "Value must be 'd' or 'c'.")
    private String tipo;
    @JsonProperty
    @NotNull
    @Size(min= 1, max = 10)
    private String descricao;
    @JsonProperty("realizada_em")
    private ZonedDateTime createdDate;

    private TransactionDTO(Long valor, String tipo, String descricao, ZonedDateTime createdDate) {
        this.valor = BigDecimal.valueOf(valor);
        this.tipo = tipo;
        this.descricao = descricao;
        this.createdDate = createdDate;
    }

    public Transaction toDomain() {
        return new Transaction(
                this.valor.toBigInteger().longValue(),
                typeDigitToType(),
                this.descricao
        );
    }

    private TransactionType typeDigitToType() {
        return switch (this.tipo) {
            case "d" -> TransactionType.DEBIT;
            case "c" -> TransactionType.CREDIT;
            default -> null;
        };
    }

    public TransactionDTO() {}

    public static TransactionDTO fromDomain(Transaction domain) {
        return new TransactionDTO(
                domain.getAmount(),
                domain.getType().name(),
                domain.getDescription(),
                domain.getDate()
        );
    }
}
