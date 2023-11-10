package ru.rosatom.documentflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserRatingDto {
    private long id;
    private final String lastName;
    private final String firstName;
    private final String email;
    private final long createdDocuments;
}