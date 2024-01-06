package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Schema(description = "Ошибка валидации")
public class ValidationError extends AppError {

    @Schema(description = "Список полей с ошибками", requiredMode = REQUIRED)
    private Map<String, String> fieldErrors;

}
