package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.FileMapper;
import com.dentalapp.backend.model.file.dtos.GetDownloadFileDto;
import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.file.exceptions.FileIsEmptyException;
import com.dentalapp.backend.model.file.exceptions.FileNameAlreadyExists;
import com.dentalapp.backend.model.file.exceptions.FileNotFoundException;
import com.dentalapp.backend.model.file.repository.FileRepository;
import com.dentalapp.backend.model.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public GetDownloadFileDto getFileById(Long id) {
        File file =  fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found"));
        return fileMapper.toGetDownloadFileDto(file);
    }

    @Transactional
    public List<GetFileDto> getFilesByAppointmentId(Appointment appointment) {
        return fileRepository.findFilesByAppointment(appointment).stream().map(fileMapper::toGetFileDto).toList();
    }

    @Transactional
    public List<GetFileDto> getFilesByUserId(User user) {
        return fileRepository.findFilesByUser(user).stream().map(fileMapper::toGetFileDto).toList();
    }

    @Transactional
    public File saveFile(MultipartFile multipartFile, Appointment appointment) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new FileIsEmptyException("File is empty");
        }
        if (doesFileNameExist(multipartFile.getOriginalFilename())) {
            throw new FileNameAlreadyExists("File with this name already exists");
        }
        File file = fileMapper.toEntity(multipartFile);
        file.setAppointment(appointment);
        return file;
    }

    @Transactional
    public void updateFile(Long id, MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new FileIsEmptyException("File is empty");
        }
        File file = fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found"));
        File updateFile = fileMapper.toUpdateEntity(file, multipartFile);
        fileRepository.save(updateFile);
    }

    public void deleteFile(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found"));
        fileRepository.delete(file);
    }

    private boolean doesFileNameExist(String fileName) {
        return fileRepository.findByFileName(fileName).isPresent();
    }
}
