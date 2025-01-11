package com.mauriciocsz.crebito.adapters.facades;

import com.mauriciocsz.crebito.adapters.repositories.UserRepository;
import com.mauriciocsz.crebito.adapters.repositories.models.UserModel;
import com.mauriciocsz.crebito.application.ports.FindUser;
import com.mauriciocsz.crebito.domain.entities.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDatabaseFacade implements FindUser {

    private UserRepository userRepository;

    @Override
    public Optional<User> byId(String id) {
        return userRepository.findById(id).map(UserModel::toDomain);
    }

    public UserDatabaseFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
