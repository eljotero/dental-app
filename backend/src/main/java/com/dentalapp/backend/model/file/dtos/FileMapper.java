package com.dentalapp.backend.model.file.dtos;

import com.dentalapp.backend.model.file.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class FileMapper {
    public static File toEntity(MultipartFile multipartFile) throws IOException {
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setFileData(multipartFile.getBytes());
        file.setUploadedAt(LocalDateTime.now());
        return file;
    }

    public static File toUpdateEntity(File file, MultipartFile multipartFile) throws IOException {
        if(!multipartFile.getName().equals(file.getFileName())) {
            file.setFileName(multipartFile.getOriginalFilename());
        }
        if(!Arrays.equals(multipartFile.getBytes(), file.getFileData())) {
            file.setFileData(multipartFile.getBytes());
        }
        file.setUpdatedAt(LocalDateTime.now());
        return file;
    }

    public static GetFileDto toGetFileDto(File file) {
        GetFileDto getFileDto = new GetFileDto();
        getFileDto.setFileId(file.getFileId());
        getFileDto.setFileName(file.getFileName());
        getFileDto.setUploadedAt(String.valueOf(file.getUploadedAt()));
        return getFileDto;
    }

    public static GetDownloadFileDto toGetDownloadFileDto(File file) {
        GetDownloadFileDto getDownloadFileDto = new GetDownloadFileDto();
        getDownloadFileDto.setFileName(file.getFileName());
        getDownloadFileDto.setFileData(file.getFileData());
        return getDownloadFileDto;
    }
}
