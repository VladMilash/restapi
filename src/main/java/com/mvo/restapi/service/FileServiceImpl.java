package com.mvo.restapi.service;

import com.mvo.restapi.exception.FileUploadException;
import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.File;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.EventRepository;
import com.mvo.restapi.repository.FileRepository;
import com.mvo.restapi.repository.UserRepository;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public File uploadFile(Part filePart, Integer userId) throws FileUploadException {
        User user = userRepository.findById(userId);

        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uniqueFileName = generateUniqueFileName(originalFileName);
        Path filePath = Paths.get("G:\\upload").resolve(uniqueFileName);

        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file", e);

        }

        File file = new File(uniqueFileName, filePath.toString());
        Event event = new Event(user, file);

        fileRepository.save(file);
        eventRepository.save(event);

        return file;
    }

    @Override
    public File getFileById(Integer id) {
        return fileRepository.findById(id);
    }

    @Override
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public File updateFile(File file) {
        return fileRepository.update(file);
    }

    @Override
    public void deleteFileById(Integer id) {
        fileRepository.deleteById(id);
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + fileExtension;
    }
}
