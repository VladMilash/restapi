package com.mvo.restapi.service;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.UserRepository;
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
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private UserServiceImpl userService;
    private User testUser;
    private Event event;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
        testUser = new User("Ivan");
        event = new Event(testUser, null);
    }

    @Test
    public void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        User createdUser = userService.createUser(testUser);
        assertEquals(testUser, createdUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getUserById() {
        when(userRepository.findById(1)).thenReturn(testUser);
        User foundUser = userService.getUserById(1);
        assertEquals(testUser, foundUser);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.findAll()).thenReturn(users);
        List<User> foundUsers = userService.getAllUsers();
        assertEquals(1, foundUsers.size());
        assertEquals(users, foundUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void updateUser() {
        when(userRepository.update(any(User.class))).thenReturn(testUser);
        User updatedUser = new User("Ivanov");
        User result = userService.updateUser(updatedUser);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).update(any(User.class));
    }


    @Test
    public void deleteUserById() {
        userService.deleteUserById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void findAllEventsByUserId() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        when(userRepository.findAllEventByUserId(1)).thenReturn(events);
        List<Event> foundEvents = userService.findAllEventsByUserId(1);
        assertEquals(1, foundEvents.size());
        assertEquals(events, foundEvents);
        verify(userRepository, times(1)).findAllEventByUserId(1);
    }

    @Test
    public void deleteAllEventsByUserId() {
        userService.deleteAllEventsByUserId(1);
        verify(userRepository, times(1)).deleteAllEventByUserId(1);
    }

    @Test
    public void addEventToUser() {
        userService.addEventToUser(1, 1);
        verify(userRepository, times(1)).addEventToUser(1, 1);
    }
}
