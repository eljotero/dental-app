package com.dentalapp.backend.model.file.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;

import java.io.Serial;

public class FileQueryParams extends PaginationParams<FileSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public FileQueryParams() {
        super(FileSortField.NAME, FileSortField.ID);
    }
}
