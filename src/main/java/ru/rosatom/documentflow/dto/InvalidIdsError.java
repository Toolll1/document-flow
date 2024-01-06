package ru.rosatom.documentflow.dto;

import lombok.Getter;

import java.util.Collection;

@Getter
public class InvalidIdsError extends AppError {
    private final Collection<Long> invalidIds;

    public InvalidIdsError(String message, Collection<Long> invalidIds) {
        super(message);
        this.invalidIds = invalidIds;
    }
}
