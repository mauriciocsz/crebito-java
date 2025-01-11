package com.mauriciocsz.crebito.adapters.facades;

import com.mauriciocsz.crebito.adapters.repositories.UserRepository;
import com.mauriciocsz.crebito.adapters.repositories.models.UserModel;
import com.mauriciocsz.crebito.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDatabaseFacadeTest {
    @InjectMocks
    private UserDatabaseFacade facade;

    @Mock
    private UserRepository userRepository;

    @Test
    void findsUserById() {
        String id = "uid";

        User expectedUser = mock(User.class);

        UserModel model = mock(UserModel.class);
        when(model.toDomain()).thenReturn(expectedUser);

        when(userRepository.findById(id)).thenReturn(Optional.of(model));

        Optional<User> result = facade.byId(id);

        assertEquals(expectedUser, result.orElse(null));
    }

    @Test
    void findsUserByIdReturnsNull() {
        String id = "uid";

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = facade.byId(id);

        assertNull(result.orElse(null));
    }
}