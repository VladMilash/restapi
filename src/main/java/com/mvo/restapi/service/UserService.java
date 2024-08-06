package com.mvo.restapi.service;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Integer id);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUserById(Integer id);

    List<Event> findAllEventsByUserId(Integer id);

    void deleteAllEventsByUserId(Integer id);

    void addEventToUser(Integer userId, Integer eventId);
}
