package com.mvo.restapi.repository;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<User, Integer> {
    List<Event> findAllEventByUserId(Integer userId);

    void deleteAllEventByUserId(Integer userId);

    void addEventToUser(Integer userId, Integer eventId);

}
