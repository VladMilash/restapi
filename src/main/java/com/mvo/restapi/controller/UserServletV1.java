package com.mvo.restapi.controller;

import com.google.gson.Gson;
import com.mvo.restapi.exception.CrudException;
import com.mvo.restapi.exception.NotExistCrudException;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.UserRepository;
import com.mvo.restapi.repository.hibernate.HibernateUserRepositoryImpl;
import com.mvo.restapi.service.UserService;
import com.mvo.restapi.service.UserServiceImpl;
import com.mvo.restapi.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/users/*")
public class UserServletV1 extends HttpServlet {

    private UserService userService;
    private UserRepository userRepository;
    private Gson gson;
    private GsonUtil gsonUtil;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepository = new HibernateUserRepositoryImpl();
        userService = new UserServiceImpl(userRepository);
        gson = new Gson();
        gsonUtil = new GsonUtil(gson);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            getAllUsers(req, resp);
        } else {
            String[] split = pathInfo.split("/");
            if (split.length == 2) {
                getUserById(req, resp, Integer.parseInt(split[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            saveUser(req, resp);
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
                updateUser(req, resp, Integer.parseInt(split[1]));
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
                deleteUser(req, resp, Integer.parseInt(split[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void getUserById(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            User user = userService.getUserById(id);
            gsonUtil.sendJsonResponse(resp, user);
        } catch (NotExistCrudException | IOException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<User> users = userService.getAllUsers();
            gsonUtil.sendJsonResponse(resp, users);
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to get Users");
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            User existingUser = userService.getUserById(id);
            User updatedUser = gson.fromJson(req.getReader(), User.class);
            updatedUser.setId(id);
            User result = userService.updateUser(updatedUser);
            gsonUtil.sendJsonResponse(resp, result);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            User user = userService.getUserById(id);
            userRepository.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void saveUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = gson.fromJson(req.getReader(), User.class);
            userService.createUser(user);
            gsonUtil.sendJsonResponse(resp, user);
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
