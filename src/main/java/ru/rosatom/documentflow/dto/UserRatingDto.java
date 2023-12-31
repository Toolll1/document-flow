package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Рейтинг пользователя")
public class UserRatingDto {

    @Schema(description = "ID пользователя", requiredMode = REQUIRED)
    private long id;

    @Schema(description = "Фамилия", requiredMode = REQUIRED)
    private final String lastName;

    @Schema(description = "Имя", requiredMode = REQUIRED)
    private final String firstName;

    @Schema(description = "Отчество")
    private final String patronymic;

    @Schema(description = "Email", requiredMode = REQUIRED)
    private final String email;

    @Schema(description = "Количество созданных документов", requiredMode = REQUIRED)
    private final long createdDocuments;
}