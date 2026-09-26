package org.example;

import org.example.console.ConsoleMenu;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.service.UserService;
import org.example.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDaoImpl();

            UserService userService = new UserService(userDao);

            ConsoleMenu consoleMenu = new ConsoleMenu(userService);

            consoleMenu.start();

        } finally {
            HibernateUtil.shutdown();
        }
    }
}