package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}