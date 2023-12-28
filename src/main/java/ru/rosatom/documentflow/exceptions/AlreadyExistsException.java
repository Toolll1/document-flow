package ru.rosatom.documentflow.exceptions;

public class AlreadyExistsException extends ConflictException {
    public AlreadyExistsException(String existingEntityName, String targetEntityName){
        super(
                String.format("%s уже существует в %s",existingEntityName, targetEntityName)
        );

    }
}
