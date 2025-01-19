package com.dentalapp.backend.model.file.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDownloadFileDto {
    private String fileName;
    private byte[] fileData;
}
