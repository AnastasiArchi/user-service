package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;

import java.util.List;
import java.util.NoSuchElementException;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(String name, String email, Integer age) {
        validateUser(name, email, age);

        User user = new User(name, email, age);
        userDao.save(user);
    }

    public User getUserById(Long id) {
        validateId(id);

        User user = userDao.findById(id);

        if (user == null) {
            throw new NoSuchElementException(
                    "Пользователь с ID " + id + " не найден"
            );
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        validateUser(name, email, age);

        User user = getUserById(id);

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        userDao.update(user);
    }

    public void deleteUser(Long id) {
        getUserById(id);
        userDao.delete(id);
    }

    private void validateUser(String name, String email, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Имя пользователя не может быть пустым"
            );
        }

        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Некорректный email"
            );
        }

        if (age == null || age < 0 || age > 150) {
            throw new IllegalArgumentException(
                    "Возраст должен быть от 0 до 150"
            );
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID пользователя должен быть положительным"
            );
        }
    }
}