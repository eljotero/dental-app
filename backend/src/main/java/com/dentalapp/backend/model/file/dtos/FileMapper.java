package com.dentalapp.backend.model.file.dtos;

import com.dentalapp.backend.model.file.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FileMapper {

    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "fileName", expression = "java(multipartFile.getOriginalFilename())")
    @Mapping(target = "fileData", expression = "java(getBytes(multipartFile))")
    @Mapping(target = "updatedAt", ignore = true)
    File toEntity(MultipartFile multipartFile);

    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "fileName", expression = "java(shouldUpdateFileName(file, multipartFile) ? multipartFile.getOriginalFilename() : file.getFileName())")
    @Mapping(target = "fileData", expression = "java(shouldUpdateFileData(file, multipartFile) ? getBytes(multipartFile) : file.getFileData())")
    @Mapping(target = "updatedAt", ignore = true)
    File toUpdateEntity(@MappingTarget File file, MultipartFile multipartFile);

    GetFileDto toGetFileDto(File file);

    GetDownloadFileDto toGetDownloadFileDto(File file);

    default byte[] getBytes(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file bytes", e);
        }
    }

    default boolean shouldUpdateFileName(File file, MultipartFile multipartFile) {
        return !multipartFile.getName().equals(file.getFileName());
    }

    default boolean shouldUpdateFileData(File file, MultipartFile multipartFile) {
        try {
            return !java.util.Arrays.equals(multipartFile.getBytes(), file.getFileData());
        } catch (IOException e) {
            throw new RuntimeException("Error comparing file data", e);
        }
    }
}
