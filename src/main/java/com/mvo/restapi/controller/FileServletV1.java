package com.mvo.restapi.controller;

import com.google.gson.Gson;
import com.mvo.restapi.exception.CrudException;
import com.mvo.restapi.exception.FileUploadException;
import com.mvo.restapi.exception.NotExistCrudException;
import com.mvo.restapi.model.File;
import com.mvo.restapi.repository.EventRepository;
import com.mvo.restapi.repository.FileRepository;
import com.mvo.restapi.repository.UserRepository;
import com.mvo.restapi.repository.hibernate.HibernateEventRepositoryImpl;
import com.mvo.restapi.repository.hibernate.HibernateFileRepositoryImpl;
import com.mvo.restapi.repository.hibernate.HibernateUserRepositoryImpl;
import com.mvo.restapi.service.FileService;
import com.mvo.restapi.service.FileServiceImpl;
import com.mvo.restapi.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@MultipartConfig
@WebServlet("/api/v1/files/*")
public class FileServletV1 extends HttpServlet {
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private FileService fileService;
    private Gson gson;
    private GsonUtil gsonUtil;

    @Override
    public void init() throws ServletException {
        super.init();
        fileRepository = new HibernateFileRepositoryImpl();
        userRepository = new HibernateUserRepositoryImpl();
        eventRepository = new HibernateEventRepositoryImpl();
        fileService = new FileServiceImpl(fileRepository, userRepository, eventRepository);
        gson = new Gson();
        gsonUtil = new GsonUtil(gson);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            getAllFiles(req, resp);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                getFileById(req, resp, Integer.parseInt(splits[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ((pathInfo == null) || (pathInfo.equals("/"))) {
            uploadFile(req, resp);
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
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                updateFile(req, resp, Integer.parseInt(splits[1]));
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
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                deleteFile(req, resp, Integer.parseInt(splits[1]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void getAllFiles(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<File> files = fileService.getAllFiles();
            gsonUtil.sendJsonResponse(resp, files);
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getFileById(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            File file = fileService.getFileById(id);
            gsonUtil.sendJsonResponse(resp, file);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void updateFile(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            File existingFile = fileService.getFileById(id);
            File updatedFile = gson.fromJson(req.getReader(), File.class);
            updatedFile.setId(id);
            File result = fileService.updateFile(updatedFile);
            gsonUtil.sendJsonResponse(resp, result);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void deleteFile(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        try {
            File file = fileService.getFileById(id);
            fileService.deleteFileById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void uploadFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Part partFile = req.getPart("file");
            Integer userId = Integer.parseInt(req.getParameter("userId"));
            File uploadFile = fileService.uploadFile(partFile, userId);
            gsonUtil.sendJsonResponse(resp, uploadFile);
        } catch (FileUploadException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to upload file");
        } catch (NotExistCrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file ID");
        } catch (CrudException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
