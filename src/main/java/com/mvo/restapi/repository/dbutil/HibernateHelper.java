package com.mvo.restapi.repository.dbutil;

import com.mvo.restapi.exception.CrudException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateHelper {
    public <T> T executeWithTransaction(HibernateExecutor executor) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T result = (T) executor.execute(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CrudException(e);
        }
    }

    public <T> T executeWithoutTransaction(HibernateExecutor executor) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return (T) executor.execute(session);
        } catch (Exception e) {
            throw new CrudException(e);
        }
    }
}