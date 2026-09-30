package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}