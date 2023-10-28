package ru.rosatom.documentflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppError {
    private String message;
}
