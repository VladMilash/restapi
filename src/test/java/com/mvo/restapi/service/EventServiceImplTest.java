package com.mvo.restapi.service;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.File;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    private EventServiceImpl eventService;

    private User testUser;
    private File testFile;
    private Event testEvent;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventServiceImpl(eventRepository);
        testUser = new User("Ivan");
        testFile = new File(1, "testFile.txt", "/path/to/file");
        testEvent = new Event(testUser, testFile);
    }

    @Test
    public void createEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        Event createdEvent = eventService.createEvent(testEvent);
        assertEquals(testEvent, createdEvent);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void getEventById() {
        when(eventRepository.findById(1)).thenReturn(testEvent);
        Event foundEvent = eventService.getEventById(1);
        assertEquals(testEvent, foundEvent);
        verify(eventRepository, times(1)).findById(1);
    }

    @Test
    public void getAllEvents() {
        List<Event> events = new ArrayList<>();
        events.add(testEvent);
        when(eventRepository.findAll()).thenReturn(events);
        List<Event> foundEvents = eventService.getAllEvents();
        assertEquals(1, foundEvents.size());
        assertEquals(events, foundEvents);
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void updateEvent() {
        when(eventRepository.update(any(Event.class))).thenReturn(testEvent);
        Event updatedEvent = new Event(testUser, testFile);
        Event result = eventService.updateEvent(updatedEvent);
        assertEquals(testEvent, result);
        verify(eventRepository, times(1)).update(any(Event.class));
    }

    @Test
    public void deleteEventById() {
        eventService.deleteEventById(1);
        verify(eventRepository, times(1)).deleteById(1);
    }
}
