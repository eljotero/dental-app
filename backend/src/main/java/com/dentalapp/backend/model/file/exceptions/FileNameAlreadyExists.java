package com.dentalapp.backend.model.file.exceptions;

public class FileNameAlreadyExists extends RuntimeException {
    public FileNameAlreadyExists(String message) {
        super(message);
    }
}
