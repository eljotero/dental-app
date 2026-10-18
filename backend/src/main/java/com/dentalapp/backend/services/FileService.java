package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.FileMapper;
import com.dentalapp.backend.model.file.dtos.GetDownloadFileDto;
import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.file.dtos.FilePageResponse;
import com.dentalapp.backend.model.file.dtos.FileQueryParams;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.file.exceptions.FileIsEmptyException;
import com.dentalapp.backend.model.file.exceptions.FileNameAlreadyExists;
import com.dentalapp.backend.model.file.exceptions.FileNotFoundException;
import com.dentalapp.backend.model.file.repository.FileRepository;
import com.dentalapp.backend.model.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class FileService {

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public GetDownloadFileDto getFileById(Long id) {
        File file = fileRepository.findById(id).orElseThrow(FileNotFoundException::new);
        return fileMapper.toGetDownloadFileDto(file);
    }

    public FilePageResponse<GetFileDto> getFilesByAppointmentId(Appointment appointment, FileQueryParams params) {
        return FilePageResponse.from(fileRepository.findFilesByAppointment(appointment, params.toPageable()).map(fileMapper::toGetFileDto));
    }

    public FilePageResponse<GetFileDto> getFilesByUserId(User user, FileQueryParams params) {
        return FilePageResponse.from(fileRepository.findFilesByUser(user, params.toPageable()).map(fileMapper::toGetFileDto));
    }

    @Transactional
    public File saveFile(MultipartFile multipartFile, Appointment appointment) {
        if (multipartFile.isEmpty()) {
            throw new FileIsEmptyException();
        }
        if (doesFileNameExist(multipartFile.getOriginalFilename())) {
            throw new FileNameAlreadyExists();
        }
        File file = fileMapper.toEntity(multipartFile);
        file.setAppointment(appointment);
        return file;
    }

    @Transactional
    public void updateFile(Long id, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new FileIsEmptyException();
        }
        File file = fileRepository.findById(id).orElseThrow(FileNotFoundException::new);
        File updateFile = fileMapper.toUpdateEntity(file, multipartFile);
        fileRepository.save(updateFile);
    }

    public void deleteFile(Long id) {
        File file = fileRepository.findById(id).orElseThrow(FileNotFoundException::new);
        fileRepository.delete(file);
    }

    private boolean doesFileNameExist(String fileName) {
        return fileRepository.findByFileName(fileName).isPresent();
    }
}
