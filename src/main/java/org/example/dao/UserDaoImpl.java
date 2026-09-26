package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger log =
            LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void save(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();
            log.info("Пользователь создан, ID: {}", user.getId());

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    log.error("Ошибка отката транзакции", rollbackException);
                }
            }
            log.error("Ошибка при создании пользователя", e);
            throw e;
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class)
                    .getResultList();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();
            log.info("Пользователь обновлён, ID: {}", user.getId());

        } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception rollbackException) {
                log.error("Ошибка отката транзакции", rollbackException);
            }
        }
        log.error("Ошибка при обновлении пользователя, ID: {}", user.getId(), e);
        throw e;
    }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            }

            transaction.commit();
            log.info("Операция удаления завершена, ID: {}", id);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    log.error("Ошибка отката транзакции", rollbackException);
                }
            }
            log.error("Ошибка при удалении пользователя, ID: {}", id, e);
            throw e;
        }
    }
}