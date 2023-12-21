package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@Schema(description = "Описание ошибки")
public class AppError {
    @Schema(description = "Текст ошибки")
    private String message;
}
