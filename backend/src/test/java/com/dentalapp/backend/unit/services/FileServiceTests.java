package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.FileMapper;
import com.dentalapp.backend.model.file.dtos.GetDownloadFileDto;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTests {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileService fileService;

    private MultipartFile multipartFile;

    private Appointment appointment;

    private MultipartFile emptyFile;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        appointment = new Appointment();
        emptyFile = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);
    }

    @Test
    void testGetFileById() {
        File file = new File();
        GetDownloadFileDto expectedDto = new GetDownloadFileDto();

        when(fileRepository.findById(1L)).thenReturn(Optional.of(file));
        when(fileMapper.toGetDownloadFileDto(file)).thenReturn(expectedDto);

        GetDownloadFileDto result = fileService.getFileById(1L);

        Assertions.assertNotNull(result);
        verify(fileRepository).findById(1L);
        verify(fileMapper).toGetDownloadFileDto(file);
    }


    @Test
    void testGetFileByIdNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.getFileById(1L));
    }

    @Test
    void testGetFilesByAppointmentId() {
        when(fileRepository.findFilesByAppointment(appointment)).thenReturn(List.of(new File()));
        fileService.getFilesByAppointmentId(appointment);
        Assertions.assertNotNull(fileService.getFilesByAppointmentId(appointment));
    }

    @Test
    void testSaveFile() {
        File file = new File();
        when(fileRepository.findByFileName(anyString())).thenReturn(Optional.empty());
        when(fileMapper.toEntity(multipartFile)).thenReturn(file);

        File savedFile = fileService.saveFile(multipartFile, appointment);

        Assertions.assertNotNull(savedFile);
        Assertions.assertEquals(appointment, savedFile.getAppointment());
    }


    @Test
    void testSaveFileEmptyFile() {
        Assertions.assertThrows(FileIsEmptyException.class, () -> fileService.saveFile(emptyFile, appointment));
    }

    @Test
    void testSaveFileNameAlreadyExists() {
        when(fileRepository.findByFileName(anyString())).thenReturn(Optional.of(new File()));
        Assertions.assertThrows(FileNameAlreadyExists.class, () -> fileService.saveFile(multipartFile, appointment));
    }

    @Test
    void testUpdateFile() {
        File file = new File();
        File updatedFile = new File();

        when(fileRepository.findById(1L)).thenReturn(Optional.of(file));
        when(fileMapper.toUpdateEntity(file, multipartFile)).thenReturn(updatedFile);
        when(fileRepository.save(updatedFile)).thenReturn(updatedFile);

        fileService.updateFile(1L, multipartFile);

        verify(fileRepository).save(updatedFile);
    }


    @Test
    void testUpdateFileIsEmpty() {
        Assertions.assertThrows(FileIsEmptyException.class, () -> fileService.updateFile(1L, emptyFile));
    }

    @Test
    void testUpdateFileNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.updateFile(1L, multipartFile));
    }

    @Test
    void testDeleteFile() {
        File file = new File();
        when(fileRepository.findById(1L)).thenReturn(Optional.of(file));
        fileService.deleteFile(1L);
        verify(fileRepository).delete(file);
    }


    @Test
    void testDeleteFileNotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(1L));
    }
}
