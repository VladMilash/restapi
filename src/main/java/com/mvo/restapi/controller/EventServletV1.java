package com.mvo.restapi.controller;

import com.google.gson.Gson;
import com.mvo.restapi.exception.CrudException;
import com.mvo.restapi.exception.NotExistCrudException;
import com.mvo.restapi.model.Event;
import com.mvo.restapi.repository.EventRepository;
import com.mvo.restapi.repository.hibernate.HibernateEventRepositoryImpl;
import com.mvo.restapi.service.EventService;
import com.mvo.restapi.service.EventServiceImpl;
import com.mvo.restapi.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/events/*")
public class EventServletV1 extends HttpServlet {
    private EventRepository eventRepository;
    private EventService eventService;
    private Gson gson;
    private GsonUtil gsonUtil;

    @Override
    public void init() throws ServletException {
        super.init();
        eventRepository = new HibernateEventRepositoryImpl();
        eventService = new EventServiceImpl(eventRepository);
        gson = new Gson();
        gsonUtil = new GsonUtil(gson);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            getAllEvents(req, resp);
        } else {
            String[] split = pathInfo.split("/");
            if (split.length == 2) {
                getEventById(req, resp, Integer.parseInt(split[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            saveEvent(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            String[] split = pathInfo.split("/");
            if (split.length == 2) {
                updateEvent(req, resp, Integer.parseInt(split[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            String[] split = pathInfo.split("/");
            if (split.length == 2) {
                deleteEvent(req, resp, Integer.parseInt(split[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void getEventById(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            Event event = eventService.getEventById(id);
            gsonUtil.sendJsonResponse(resp, event);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getAllEvents(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Event> events = eventService.getAllEvents();
            gsonUtil.sendJsonResponse(resp, events);
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void updateEvent(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            Event existingEvent = eventService.getEventById(id);
            Event updatedEvent = gson.fromJson(req.getReader(), Event.class);
            updatedEvent.setId(id);
            Event result = eventService.updateEvent(updatedEvent);
            gsonUtil.sendJsonResponse(resp, result);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void deleteEvent(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            Event event = eventService.getEventById(id);
            eventRepository.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void saveEvent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Event event = gson.fromJson(req.getReader(), Event.class);
            eventService.createEvent(event);
            gsonUtil.sendJsonResponse(resp, event);
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
