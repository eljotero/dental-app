package com.dentalapp.backend.model.file.exceptions;

public class FileIsEmptyException extends RuntimeException {
    public FileIsEmptyException() {
        super("File is empty");
    }
}
