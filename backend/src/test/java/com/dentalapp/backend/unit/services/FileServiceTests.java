package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.file.exceptions.FileIsEmptyException;
import com.dentalapp.backend.model.file.exceptions.FileNameAlreadyExists;
import com.dentalapp.backend.model.file.exceptions.FileNotFoundException;
import com.dentalapp.backend.model.file.repository.FileRepository;
import com.dentalapp.backend.model.user.entity.User;
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

    @Test
    public void testGetFileById() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(new File()));
        fileService.getFileById(1L);
    }

    @Test
    public void testGetFileByIdNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.getFileById(1L));
    }

    @Test
    public void testGetFilesByAppointmentId() {
        when(fileRepository.findFilesByAppointment(appointment)).thenReturn(List.of(new File()));
        fileService.getFilesByAppointmentId(appointment);
        Assertions.assertNotNull(fileService.getFilesByAppointmentId(appointment));
    }

    @Test
    public void testSaveFile() throws IOException {
        when(fileRepository.findByFileName(anyString())).thenReturn(Optional.empty());
        when(fileRepository.save(any(File.class))).thenReturn(new File());
        File savedFile = fileService.saveFile(multipartFile, appointment);
        Assertions.assertNotNull(savedFile);
    }

    @Test
    public void testSaveFileEmptyFile() {
        Assertions.assertThrows(FileIsEmptyException.class, () -> fileService.saveFile(emptyFile, appointment));
    }

    @Test
    public void testSaveFileNameAlreadyExists() {
        when(fileRepository.findByFileName(anyString())).thenReturn(Optional.of(new File()));
        Assertions.assertThrows(FileNameAlreadyExists.class, () -> fileService.saveFile(multipartFile, appointment));
    }

    @Test
    public void testUpdateFile() throws IOException {
        File file = new File();
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));
        when(fileRepository.save(any(File.class))).thenReturn(new File());
        fileService.updateFile(1L, multipartFile);
    }

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

    @Test
    public void testGetFilesByUserId() {
        User user = new User();
        when(fileRepository.findFilesByUser(user)).thenReturn(List.of(new File()));
        List<GetFileDto> result = fileService.getFilesByUserId(user);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testIsUsersFile() {
        Appointment appointment = new Appointment();
        User user = new User();
        user.setEmail("test@mail.com");
        User doctor = new User();
        doctor.setEmail("test2@mail.com");
        appointment.setDoctor(doctor);
        appointment.setPatient(user);
        File file = new File();;
        file.setAppointment(appointment);
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));
        Assertions.assertTrue(fileService.isUsersFile(1L, user.getEmail()));
        Assertions.assertTrue(fileService.isUsersFile(1L, doctor.getEmail()));
    }

    @Test
    public void testIsUsersFileNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.isUsersFile(1L, "test@mail.com"));
    }
}
