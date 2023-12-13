package ru.rosatom.documentflow.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class ValidationError extends AppError {
    private Map<String, String> fieldErrors;

}
