package com.mauriciocsz.crebito.application.ports;

import com.mauriciocsz.crebito.domain.entities.User;

import java.util.Optional;

public interface FindUser {
    Optional<User> byId(String id);
}
