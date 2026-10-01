package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void shouldReturnUserById() {
        UserDao userDao = mock(UserDao.class);

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        when(userDao.findById(1L))
                .thenReturn(user);

        UserService userService = new UserService(userDao);

        User result = userService.getUserById(1L);

        assertEquals("Anna", result.getName());
        assertEquals("anna@test.com", result.getEmail());
        assertEquals(25, result.getAge());
    }

    @Test
    void shouldCreateUser() {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        userService.createUser(
                "Anna",
                "anna@test.com",
                25
        );

        verify(userDao).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Anna", "wrong-email", 25)
        );
    }

    @Test
    void shouldThrowExceptionWhenAgeIsInvalid() {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Anna", "anna@test.com", -1)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserService(userDao);

        when(userDao.findById(1000L))
                .thenReturn(null);

        assertThrows(
                NoSuchElementException.class,
                () -> userService.getUserById(1000L)
        );
    }

    @Test
    void shouldReturnAllUsers() {
        UserDao userDao = mock(UserDao.class);

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

        when(userDao.findAll()).thenReturn(List.of(user1, user2));
        UserService userService = new UserService(userDao);
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void shouldUpdateUser() {
        UserDao userDao = mock(UserDao.class);

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        when(userDao.findById(1L)).thenReturn(user);
        UserService userService = new UserService(userDao);
        userService.updateUser(
                1L,
                "Maria",
                "maria@test.com",
                30
        );

        assertEquals("Maria", user.getName());
        assertEquals("maria@test.com", user.getEmail());
        assertEquals(30, user.getAge());

        verify(userDao).update(user);
    }

    @Test
    void shouldDeleteUser() {
        UserDao userDao = mock(UserDao.class);

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        when(userDao.findById(1L)).thenReturn(user);
        UserService userService = new UserService(userDao);
        userService.deleteUser(1L);

        verify(userDao).delete(1L);
    }
}