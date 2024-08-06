package com.mvo.restapi.service;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);

    }

    @Override
    public List<Event> findAllEventsByUserId(Integer id) {
        return userRepository.findAllEventByUserId(id);
    }

    @Override
    public void deleteAllEventsByUserId(Integer id) {
        userRepository.deleteAllEventByUserId(id);

    }

    @Override
    public void addEventToUser(Integer userId, Integer eventId) {
        userRepository.addEventToUser(userId, eventId);
    }
}
