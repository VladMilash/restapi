package com.mvo.restapi.service;

import com.mvo.restapi.model.File;
import com.mvo.restapi.model.User;
import com.mvo.restapi.repository.EventRepository;
import com.mvo.restapi.repository.FileRepository;
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
public class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;

    private FileServiceImpl fileService;

    private File testFile;

    private User testUser;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fileService = new FileServiceImpl(fileRepository, userRepository, eventRepository);
        testFile = new File(1, "testFile.txt", "/path/to/file");
        testUser = new User("Ivan");
    }

    @Test
    public void getFileById() {
        when(fileRepository.findById(1)).thenReturn(testFile);
        File foundFile = fileService.getFileById(1);
        assertEquals(testFile, foundFile);
        verify(fileRepository, times(1)).findById(1);
    }

    @Test
    public void getAllFiles() {
        List<File> files = new ArrayList<>();
        files.add(testFile);
        when(fileRepository.findAll()).thenReturn(files);
        List<File> foundFiles = fileService.getAllFiles();
        assertEquals(1, foundFiles.size());
        assertEquals(files, foundFiles);
        verify(fileRepository, times(1)).findAll();
    }

    @Test
    public void updateFile() {
        when(fileRepository.update(any(File.class))).thenReturn(testFile);
        File updatedFile = new File(1, "updatedFile.txt", "/new/path/to/file");
        File result = fileService.updateFile(updatedFile);
        assertEquals(testFile, result);
        verify(fileRepository, times(1)).update(any(File.class));
    }

    @Test
    public void deleteFileById() {
        fileService.deleteFileById(1);
        verify(fileRepository, times(1)).deleteById(1);
    }
}
