package com.mvo.restapi.service;

import com.mvo.restapi.model.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    Event getEventById(Integer id);

    List<Event> getAllEvents();

    Event updateEvent(Event event);

    void deleteEventById(Integer id);
}
