package com.dentalapp.backend.model.file.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFileDto {
    private Long fileId;
    private String fileName;
    private String uploadedAt;
    private String updatedAt;
}
