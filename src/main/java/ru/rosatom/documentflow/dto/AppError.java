package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "Описание ошибки")
public class AppError {
    @Schema(name = "Текст ошибки")
    private String message;
}
