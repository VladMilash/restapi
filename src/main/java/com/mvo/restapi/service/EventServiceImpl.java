package com.mvo.restapi.service;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.repository.EventRepository;

import java.util.List;

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.update(event);
    }

    @Override
    public void deleteEventById(Integer id) {
        eventRepository.deleteById(id);
    }
}
