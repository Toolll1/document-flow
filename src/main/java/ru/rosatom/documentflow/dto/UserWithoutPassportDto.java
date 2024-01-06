package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserWithoutPassportDto {


    @Schema(description = "ID пользователя", requiredMode = REQUIRED)
    private Long id;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 100, requiredMode = REQUIRED)
    private String lastName;

    @Schema(description = "Имя", minLength = 2, maxLength = 100, requiredMode = REQUIRED)
    private String firstName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 100, requiredMode = REQUIRED)
    private String patronymic;

    @Schema(description = "Дата рождения", minLength = 10, maxLength = 10, requiredMode = REQUIRED)
    private String dateOfBirth;

    @Schema(description = "Email", minLength = 6, maxLength = 320, requiredMode = REQUIRED)
    private String email;

    @Schema(description = "Номер телефона", minLength = 11, maxLength = 11, requiredMode = REQUIRED)
    private String phone;

    @Schema(description = "Описание пользователя", minLength = 1, maxLength = 320, requiredMode = REQUIRED)
    private String post;

    @Schema(description = "Роль пользователя", requiredMode = REQUIRED)
    private String role;

    @Schema(description = "ID организации", requiredMode = REQUIRED)
    private OrgDto organization;
}