package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Данные для создания пользователя")
public class UserCreateDto {


    @Schema(description = "Фамилия", minLength = 2, maxLength = 100, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 2, max = 100)
    private final String lastName;

    @Schema(description = "Имя", minLength = 2, maxLength = 100, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 2, max = 100)
    private final String firstName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 100)
    @Size(min = 2, max = 100)
    private final String patronymic;

    @Schema(description = "Дата рождения", requiredMode = REQUIRED)
    @NotNull
    private final LocalDate dateOfBirth;

    @Schema(description = "Email", minLength = 6, maxLength = 320, requiredMode = REQUIRED)
    @Email
    @NotBlank
    @Size(min = 6, max = 320)
    private final String email;

    @Schema(description = "Номер телефона", minLength = 11, maxLength = 11, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 11, max = 11)
    private final String phone;

    @Schema(description = "Серия паспорта", minLength = 4, maxLength = 4, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 4, max = 4)
    private final String passportSeries;

    @Schema(description = "Номер паспорта", minLength = 6, maxLength = 6, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 6, max = 6)
    private final String passportNumber;

    @Schema(description = "Кем выдан паспорт", minLength = 2, maxLength = 1000, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 2, max = 1000)
    private final String passportIssued; // кем выдан

    @Schema(description = "Дата выдачи паспорта", requiredMode = REQUIRED)
    @NotNull
    private final LocalDate passportDate;

    @Schema(description = "Код подразделения", minLength = 6, maxLength = 6, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 6, max = 6)
    private final String passportKp; // код подразделения

    @Schema(description = "ID организации", requiredMode = REQUIRED)
    @NotNull
    private final Long organizationId;

    @Schema(description = "Роль пользователя", requiredMode = REQUIRED)
    @NotNull
    private final String role;

    @Schema(description = "Описание пользователя", minLength = 1, maxLength = 320, requiredMode = REQUIRED)
    @NotBlank
    @Size(min = 1, max = 320)
    private String post;
}