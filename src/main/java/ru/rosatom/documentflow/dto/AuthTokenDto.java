package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(force = true)
public class AuthTokenDto {
    private String token;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 100)
    private String lastName;

    @Schema(description = "Имя", minLength = 2, maxLength = 100)
    private String firstName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 100)
    private String patronymic;

    @Schema(description = "Дата рождения", minLength = 10, maxLength = 10)
    private String dateOfBirth;

    @Schema(description = "Email", minLength = 6, maxLength = 320)
    private String email;

    @Schema(description = "Номер телефона", minLength = 11, maxLength = 11)
    private String phone;

    @Schema(description = "ID организации")
    private Long organizationId;

    @Schema(description = "Роль пользователя")
    private String role;

    @Schema(description = "Описание пользователя", minLength = 1, maxLength = 320)
    private String post;
}