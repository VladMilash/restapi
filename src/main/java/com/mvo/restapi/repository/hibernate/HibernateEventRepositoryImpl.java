package com.mvo.restapi.repository.hibernate;

import com.mvo.restapi.exception.NotExistCrudException;
import com.mvo.restapi.model.Event;
import com.mvo.restapi.repository.EventRepository;
import com.mvo.restapi.repository.dbutil.HibernateHelper;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateEventRepositoryImpl implements EventRepository {

    private final HibernateHelper hibernateHelper = new HibernateHelper();

    @Override
    public Event findById(Integer id) {
        return hibernateHelper.executeWithoutTransaction(session -> {
            Event event = fetchJoinEventSelect(session, id);

            if (event == null) {
                throw new NotExistCrudException(id);
            }
            return event;
        });
    }

    @Override
    public List<Event> findAll() {
        return hibernateHelper.executeWithoutTransaction(session -> {
            String eventHql = "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.file LEFT JOIN FETCH e.user";
            Query<Event> eventQuery = session.createQuery(eventHql, Event.class);
            return eventQuery.getResultList();
        });
    }

    @Override
    public Event save(Event event) {
        return hibernateHelper.executeWithTransaction(session -> {
            session.persist(event);
            return event;
        });
    }

    @Override
    public Event update(Event event) {
        return hibernateHelper.executeWithTransaction(session -> {
            session.merge(event);
            return event;
        });
    }

    @Override
    public void deleteById(Integer id) {
        hibernateHelper.executeWithTransaction(session -> {
            Event event = fetchJoinEventSelect(session, id);

            if (event == null) {
                throw new NotExistCrudException(id);
            }

            session.remove(event);
            return null;
        });
    }

    private Event fetchJoinEventSelect(Session session, Integer id) {
        String eventHql = "SELECT e FROM Event e LEFT JOIN FETCH e.file LEFT JOIN FETCH e.user WHERE e.id = :id";
        Query<Event> eventQuery = session.createQuery(eventHql, Event.class);
        eventQuery.setParameter("id", id);
        return eventQuery.uniqueResult();
    }
}
