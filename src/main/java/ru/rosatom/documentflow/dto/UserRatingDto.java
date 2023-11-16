package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(name = "Рейтинг пользователя")
public class UserRatingDto {

    @Schema(name = "ID пользователя")
    private long id;

    @Schema(name = "Фамилия")
    private final String lastName;

    @Schema(name = "Имя")
    private final String firstName;

    @Schema(name = "Email")
    private final String email;

    @Schema(name = "Количество созданных документов")
    private final long createdDocuments;
}