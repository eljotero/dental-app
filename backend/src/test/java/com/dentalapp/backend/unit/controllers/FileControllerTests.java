package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.FileController;
import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileControllerTests {
    @Mock
    private FileService fileService;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private FileController fileController;

    private MultipartFile multipartFile;

    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes());
        file = new File();
        file.setFileData(multipartFile.getBytes());
    }

    @Test
    public void testGetFileById(){
        when(fileService.getFileById(1L)).thenReturn(file.getFileData());
        ResponseEntity<byte[]> response = fileController.getFileById(1L);
        Assertions.assertEquals(file.getFileData(), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetFilesByAppointmentId() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(null);
        when(fileService.getFilesByAppointmentId(null)).thenReturn(null);
        ResponseEntity<List<GetFileDto>> response = fileController.getFilesByAppointmentId(1L);
        Assertions.assertNull(response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testUploadFile() throws IOException {
        doNothing().when(appointmentService).uploadFileToAppointment(1L, multipartFile);
        ResponseEntity<String> response = fileController.uploadFile(multipartFile, 1L);
        Assertions.assertEquals("File uploaded successfully", response.getBody());
        Assertions.assertEquals(201, response.getStatusCode().value());
    }

    @Test
    public void testUpdateFile() throws IOException {
        doNothing().when(fileService).updateFile(1L, multipartFile);
        ResponseEntity<String> response = fileController.updateFile(1L, multipartFile);
        Assertions.assertEquals("File updated successfully", response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testDeleteFile() {
        doNothing().when(fileService).deleteFile(1L);
        ResponseEntity<String> response = fileController.deleteFile(1L);
        Assertions.assertEquals("File deleted successfully", response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }
}
