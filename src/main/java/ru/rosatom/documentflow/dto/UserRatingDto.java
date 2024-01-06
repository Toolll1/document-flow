package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Рейтинг пользователя")
public class UserRatingDto {

    @Schema(description = "ID пользователя")
    private long id;

    @Schema(description = "Фамилия")
    private final String lastName;

    @Schema(description = "Имя")
    private final String firstName;

    @Schema(description = "Отчество")
    private final String patronymic;

    @Schema(description = "Email")
    private final String email;

    @Schema(description = "Количество созданных документов")
    private final long createdDocuments;
}