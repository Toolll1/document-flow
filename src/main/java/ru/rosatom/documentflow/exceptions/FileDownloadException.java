package ru.rosatom.documentflow.exceptions;

public class FileDownloadException extends RuntimeException {
    public FileDownloadException(String message) {
        super(message);
    }
}
