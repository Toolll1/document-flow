package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Collection;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "Ошибка, содержащая список невалидных (не найденных) идентификаторов")
public class InvalidIdsError extends AppError {

    @Schema(description = "Список невалидных идентификаторов", requiredMode = REQUIRED)
    private final Collection<Long> invalidIds;

    public InvalidIdsError(String message, Collection<Long> invalidIds) {
        super(message);
        this.invalidIds = invalidIds;
    }
}
