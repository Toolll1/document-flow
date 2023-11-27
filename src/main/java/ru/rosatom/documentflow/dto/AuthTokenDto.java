package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@Data
public class AuthTokenDto {
    private String token;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 100)
    @NotBlank
    @Size(min = 2, max = 100)
    private final String lastName;

    @Schema(description = "Имя", minLength = 2, maxLength = 100)
    @NotBlank
    @Size(min = 2, max = 100)
    private final String firstName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 100)
    @Size(min = 2, max = 100)
    private final String patronymic;

    @Schema(description = "Дата рождения", minLength = 10, maxLength = 10)
    @NotBlank
    @Size(min = 10, max = 10)
    private final String dateOfBirth;

    @Schema(description = "Email", minLength = 6, maxLength = 320)
    @Email
    @NotBlank
    @Size(min = 6, max = 320)
    private final String email;

    @Schema(description = "Номер телефона", minLength = 11, maxLength = 11)
    @NotBlank
    @Size(min = 11, max = 11)
    private final String phone;

    @Schema(description = "ID организации")
    @NotNull
    private final Long organizationId;

    @Schema(description = "Роль пользователя")
    @NotNull
    private final String role;

    @Schema(description = "Описание пользователя", minLength = 1, maxLength = 320)
    @NotBlank
    @Size(min = 1, max = 320)
    private String post;
}
