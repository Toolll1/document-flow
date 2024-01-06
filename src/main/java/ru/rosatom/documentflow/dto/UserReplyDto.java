package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Schema(description = "Ответ на получение пользователя")
public class UserReplyDto {

    @Schema(description = "ID пользователя", requiredMode = REQUIRED)
    private final Long id;

    @Schema(description = "Фамилия", requiredMode = REQUIRED)
    private final String lastName;

    @Schema(description = "Имя", requiredMode = REQUIRED)
    private final String firstName;

    @Schema(description = "Отчество")
    private final String patronymic;

    @Schema(description = "Дата рождения", requiredMode = REQUIRED)
    private final LocalDate dateOfBirth;

    @Schema(description = "Email", requiredMode = REQUIRED)
    private final String email;

    @Schema(description = "Телефон", requiredMode = REQUIRED)
    private final String phone;

    @Schema(description = "Описание", requiredMode = REQUIRED)
    private final String post;

    @Schema(description = "Роль", requiredMode = REQUIRED)
    private final String role;

    @Schema(description = "Организация", requiredMode = REQUIRED)
    private final OrgDto organization;

}
