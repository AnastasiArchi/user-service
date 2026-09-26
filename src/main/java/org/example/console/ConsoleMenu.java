package org.example.console;

import org.example.entity.User;
import org.example.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;
import org.hibernate.exception.ConstraintViolationException;

public class ConsoleMenu {

    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> createUser();
                    case "2" -> findUser();
                    case "3" -> showAllUsers();
                    case "4" -> updateUser();
                    case "5" -> deleteUser();
                    case "0" -> {
                        running = false;
                        System.out.println("Завершение работы.");
                    }
                    default -> System.out.println("Неизвестная команда.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка ввода: " + e.getMessage());

            } catch (NoSuchElementException e) {
                System.out.println("Ошибка: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("Ошибка при работе с базой данных.");
            }
        }
    }

    private void printMenu() {
        System.out.println("""

                ===== USER SERVICE =====
                1. Создать пользователя
                2. Найти пользователя по ID
                3. Показать всех пользователей
                4. Обновить пользователя
                5. Удалить пользователя
                0. Выход

                Выберите действие:
                """);
    }

    private void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        int age = readInt("Введите возраст: ");

        userService.createUser(name, email, age);

        System.out.println("Пользователь успешно создан!");
    }

    private void findUser() {
        long id = readLong("Введите ID: ");

        User user = userService.getUserById(id);

        printUser(user);
    }

    private void showAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
            return;
        }

        users.forEach(this::printUser);
    }

    private void updateUser() {
        long id = readLong("Введите ID: ");

        System.out.print("Введите новое имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите новый email: ");
        String email = scanner.nextLine();

        int age = readInt("Введите новый возраст: ");

        userService.updateUser(id, name, email, age);

        System.out.println("Пользователь успешно обновлён!");
    }

    private void deleteUser() {
        long id = readLong("Введите ID: ");

        userService.deleteUser(id);

        System.out.println("Пользователь успешно удалён!");
    }

    private void printUser(User user) {
        System.out.println("------------------------");
        System.out.println("ID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Дата создания: " + user.getCreatedAt());
    }

    private int readInt(String message) {
        System.out.print(message);
        String input = scanner.nextLine();

        if (input.isBlank()) {
            throw new IllegalArgumentException("Возраст не может быть пустым");
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Возраст должен быть числом");
        }
    }

    private long readLong(String message) {
        System.out.print(message);
        return Long.parseLong(scanner.nextLine());
    }
}