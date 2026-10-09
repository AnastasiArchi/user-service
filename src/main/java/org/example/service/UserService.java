package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email, Integer age) {
        validateUser(name, email, age);

        User user = new User(name, email, age);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        validateId(id);

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException(
                    "Пользователь с ID " + id + " не найден"
            );
        }

        return optionalUser.get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        validateUser(name, email, age);

        User user = getUserById(id);

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
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