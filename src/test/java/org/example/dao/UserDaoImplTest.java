package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    @BeforeEach
    void cleanDatabase() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createMutationQuery("delete from User")
                    .executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void shouldFindAllUsers() {
        UserDao userDao = new UserDaoImpl();
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

        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void shouldSaveAndFindUser() {
        UserDao userDao = new UserDaoImpl();

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );
        userDao.save(user);
        User savedUser = userDao.findById(user.getId());

        assertNotNull(savedUser);
        assertEquals("Anna", savedUser.getName());
        assertEquals("anna@test.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    void shouldUpdateUser() {
        UserDao userDao = new UserDaoImpl();

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        userDao.save(user);

        user.setName("Maria");
        user.setEmail("maria@test.com");
        user.setAge(30);

        userDao.update(user);

        User updatedUser = userDao.findById(user.getId());

        assertNotNull(updatedUser);
        assertEquals("Maria", updatedUser.getName());
        assertEquals("maria@test.com", updatedUser.getEmail());
        assertEquals(30, updatedUser.getAge());
    }

    @Test
    void shouldDeleteUser() {
        UserDao userDao = new UserDaoImpl();

        User user = new User(
                "Anna",
                "anna@test.com",
                25
        );

        userDao.save(user);
        Long userId = user.getId();
        userDao.delete(userId);

        User deletedUser = userDao.findById(userId);

        assertNull(deletedUser);
    }
}