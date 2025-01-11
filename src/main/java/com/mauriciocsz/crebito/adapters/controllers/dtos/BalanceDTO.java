package com.mauriciocsz.crebito.adapters.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauriciocsz.crebito.domain.entities.User;

import java.time.ZonedDateTime;

public class BalanceDTO {
    @JsonProperty
    private Long total;

    @JsonProperty("data_extrato")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime dataExtrato;

    @JsonProperty
    private Long limite;

    public BalanceDTO(Long total, ZonedDateTime dataExtrato, Long limite) {
        this.total = total;
        this.dataExtrato = dataExtrato;
        this.limite = limite;
    }

    public static BalanceDTO fromDomain(User user, ZonedDateTime date) {
        return new BalanceDTO(user.balance(), date, user.limit());
    }
}
