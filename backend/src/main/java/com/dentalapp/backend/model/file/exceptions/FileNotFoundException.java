package com.dentalapp.backend.model.file.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("File not found");
    }
}
