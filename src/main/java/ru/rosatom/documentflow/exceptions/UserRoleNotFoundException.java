package ru.rosatom.documentflow.exceptions;

public class UserRoleNotFoundException extends RuntimeException{
    public UserRoleNotFoundException(String message) {
        super(message);
    }
}
