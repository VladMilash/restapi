package com.mvo.restapi.service;

import com.mvo.restapi.exception.FileUploadException;
import com.mvo.restapi.model.File;

import javax.servlet.http.Part;
import java.nio.file.Path;
import java.util.List;

public interface FileService {
    File uploadFile(Part filePart , Integer userId) throws FileUploadException;

    File getFileById(Integer id);

    List<File> getAllFiles();

    File updateFile(File file);

    void deleteFileById(Integer id);
}
