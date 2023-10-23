package ru.rosatom.documentflow.exceptions;

public class ConflictException extends IllegalArgumentException {

    public ConflictException(String message) {
        super(message);
    }
}