package com.mvo.restapi.repository.hibernate;

import com.mvo.restapi.exception.NotExistCrudException;
import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.UserRepository;
import com.mvo.restapi.repository.dbutil.HibernateHelper;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {

    private final HibernateHelper hibernateHelper = new HibernateHelper();

    @Override
    public List<Event> findAllEventByUserId(Integer userId) {
        return hibernateHelper.executeWithoutTransaction(session -> {
            String userCheckHql = "SELECT 1 FROM User u WHERE u.id = :userId";
            Query<Integer> userCheckQuery = session.createQuery(userCheckHql, Integer.class);
            userCheckQuery.setParameter("userId", userId);

            if (userCheckQuery.uniqueResult() == null) {
                throw new NotExistCrudException(userId);
            }

            String eventHql = "SELECT e FROM Event e LEFT JOIN FETCH e.file WHERE e.user.id = :userId";
            Query<Event> eventQuery = session.createQuery(eventHql, Event.class);
            eventQuery.setParameter("userId", userId);

            return eventQuery.getResultList();
        });
    }

    @Override
    public void deleteAllEventByUserId(Integer userId) {
        hibernateHelper.executeWithTransaction(session -> {
            String userHql = "SELECT u FROM User u WHERE u.id = :userId";
            Query<User> userQuery = session.createQuery(userHql, User.class);
            userQuery.setParameter("userId", userId);
            User user = userQuery.uniqueResult();

            if (user == null) {
                throw new NotExistCrudException(userId);
            }

            for (Event event : user.getEvents()) {
                session.remove(event);
            }

            user.getEvents().clear();

            return null;
        });
    }


    @Override
    public void addEventToUser(Integer userId, Integer eventId) {
        hibernateHelper.executeWithTransaction(session -> {
            User user = session.get(User.class, userId);
            Event event = session.get(Event.class, eventId);
            if (user == null || event == null) {
                throw new NotExistCrudException(userId, eventId);
            }
            Hibernate.initialize(user.getEvents());
            Hibernate.initialize(event.getFile());
            user.getEvents().add(event);
            session.merge(user);
            return null;
        });
    }

    @Override
    public User findById(Integer id) {
        return hibernateHelper.executeWithoutTransaction(session -> {
            String userHql = "SELECT u FROM User u LEFT JOIN FETCH u.events e LEFT JOIN FETCH e.file WHERE u.id = :id";
            Query<User> userQuery = session.createQuery(userHql, User.class);
            userQuery.setParameter("id", id);
            User user = userQuery.uniqueResult();

            if (user == null) {
                throw new NotExistCrudException(id);
            }
            return user;
        });
    }

    @Override
    public List<User> findAll() {
        return hibernateHelper.executeWithoutTransaction(session -> {
            String userHql = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.events e LEFT JOIN FETCH e.file";
            Query<User> userQuery = session.createQuery(userHql, User.class);
            return userQuery.getResultList();
        });
    }

    @Override
    public User save(User user) {
        return hibernateHelper.executeWithTransaction(session -> {
            session.persist(user);
            return user;
        });
    }

    @Override
    public User update(User user) {
        return hibernateHelper.executeWithTransaction(session -> {
            session.merge(user);
            return user;
        });
    }

    @Override
    public void deleteById(Integer id) {
        hibernateHelper.executeWithTransaction(session -> {
            String userHql = "SELECT u FROM User u LEFT JOIN FETCH u.events e LEFT JOIN FETCH e.file WHERE u.id = :id";
            Query<User> userQuery = session.createQuery(userHql, User.class);
            userQuery.setParameter("id", id);
            User user = userQuery.uniqueResult();

            if (user == null) {
                throw new NotExistCrudException(id);
            }
            session.remove(user);
            return null;
        });
    }
}
