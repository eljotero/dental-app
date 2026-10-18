package com.dentalapp.backend.model.file.exceptions;

public class FileNameAlreadyExists extends RuntimeException {
    public FileNameAlreadyExists() {
        super("File with this name already exists");
    }
}
