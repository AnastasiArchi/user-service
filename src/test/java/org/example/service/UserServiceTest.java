package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.example.kafka.UserEventProducer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    @Test
    void shouldReturnUserById() {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        User result = userService.getUserById(1L);

        assertEquals("Anna", result.getName());
        assertEquals("anna@test.com", result.getEmail());
        assertEquals(25, result.getAge());
    }

    @Test
    void shouldCreateUser() {
        UserRepository userRepository = mock(UserRepository.class);
        UserEventProducer userEventProducer = mock(UserEventProducer.class);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserService userService = new UserService(
                userRepository,
                userEventProducer
        );

        userService.createUser(
                "Anna",
                "anna@test.com",
                25
        );

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Anna", "wrong-email", 25)
        );
    }

    @Test
    void shouldThrowExceptionWhenAgeIsInvalid() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Anna", "anna@test.com", -1)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        when(userRepository.findById(1000L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> userService.getUserById(1000L)
        );
    }

    @Test
    void shouldReturnAllUsers() {
        UserRepository userRepository = mock(UserRepository.class);

        User user1 = new User(
                "Anna",
                "anna@test.com",
                25
        );

        User user2 = new User(
                "Anton",
                "anton@test.com",
                30
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void shouldUpdateUser() {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        userService.updateUser(
                1L,
                "Maria",
                "maria@test.com",
                30
        );

        assertEquals("Maria", user.getName());
        assertEquals("maria@test.com", user.getEmail());
        assertEquals(30, user.getAge());

        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUser() {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserService userService = new UserService(userRepository, mock(UserEventProducer.class));

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}