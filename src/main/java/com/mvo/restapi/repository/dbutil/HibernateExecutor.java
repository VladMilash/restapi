package com.mvo.restapi.repository.dbutil;

import org.hibernate.Session;

public interface HibernateExecutor <T> {
    T execute(Session session) throws Exception;
}
