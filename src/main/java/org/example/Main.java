package org.example;

import org.example.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate успешно подключен к PostgreSQL!");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}