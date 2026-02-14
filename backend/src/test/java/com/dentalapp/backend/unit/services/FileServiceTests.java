package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.file.exceptions.FileIsEmptyException;
import com.dentalapp.backend.model.file.exceptions.FileNameAlreadyExists;
import com.dentalapp.backend.model.file.exceptions.FileNotFoundException;
import com.dentalapp.backend.model.file.repository.FileRepository;
import com.dentalapp.backend.services.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceTests {
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    private MultipartFile multipartFile;

    private Appointment appointment;

    private MultipartFile emptyFile;

    @BeforeEach
    public void setUp() {
        multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        appointment = new Appointment();
        emptyFile = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);
    }

//    @Test
//    public void testGetFileById() {
//        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(new File()));
//        fileService.getFileById(1L);
//    }

    @Test
    public void testGetFileByIdNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.getFileById(1L));
    }

//    @Test
//    public void testGetFilesByAppointmentId() {
//        when(fileRepository.findFilesByAppointment(appointment)).thenReturn(List.of(new File()));
//        fileService.getFilesByAppointmentId(appointment);
//        Assertions.assertNotNull(fileService.getFilesByAppointmentId(appointment));
//    }

//    @Test
//    public void testSaveFile() throws IOException {
//        when(fileRepository.findByFileName(anyString())).thenReturn(Optional.empty());
//        File savedFile = fileService.saveFile(multipartFile, appointment);
//        Assertions.assertNotNull(savedFile);
//    }

    @Test
    public void testSaveFileEmptyFile() {
        Assertions.assertThrows(FileIsEmptyException.class, () -> fileService.saveFile(emptyFile, appointment));
    }

    @Test
    public void testSaveFileNameAlreadyExists() {
        when(fileRepository.findByFileName(anyString())).thenReturn(Optional.of(new File()));
        Assertions.assertThrows(FileNameAlreadyExists.class, () -> fileService.saveFile(multipartFile, appointment));
    }

//    @Test
//    public void testUpdateFile() throws IOException {
//        File file = new File();
//        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));
//        when(fileRepository.save(any(File.class))).thenReturn(new File());
//        fileService.updateFile(1L, multipartFile);
//    }

    @Test
    public void testUpdateFileIsEmpty() {
        Assertions.assertThrows(FileIsEmptyException.class, () -> fileService.updateFile(1L, emptyFile));
    }

    @Test
    public void testUpdateFileNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.updateFile(1L, multipartFile));
    }

    @Test
    public void testDeleteFile() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(new File()));
        fileService.deleteFile(1L);
    }

    @Test
    public void testDeleteFileNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(1L));
    }
}
