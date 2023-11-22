package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
@Schema(name = "Данные для создания пользователя")
public class UserCreateDto {
    @Schema(name = "ID пользователя")
    private final Long id;

    @Schema(name = "Фамилия", minLength = 2, maxLength = 100)
    @NotBlank
    @Size(min = 2, max = 100)
    private final String lastName;

    @Schema(name = "Имя", minLength = 2, maxLength = 100)
    @NotBlank
    @Size(min = 2, max = 100)
    private final String firstName;

    @Schema(name = "Отчество", minLength = 2, maxLength = 100)
    @Size(min = 2, max = 100)
    private final String patronymic;

    @Schema(name = "Дата рождения", minLength = 10, maxLength = 10)
    @NotBlank
    @Size(min = 10, max = 10)
    private final String dateOfBirth;

    @Schema(name = "Email", minLength = 6, maxLength = 320)
    @Email
    @NotBlank
    @Size(min = 6, max = 320)
    private final String email;

    @Schema(name = "Номер телефона", minLength = 11, maxLength = 11)
    @NotBlank
    @Size(min = 11, max = 11)
    private final String phone;

    @Schema(name = "Серия паспорта", minLength = 4, maxLength = 4)
    @NotBlank
    @Size(min = 4, max = 4)
    private final String passportSeries;

    @Schema(name = "Номер паспорта", minLength = 6, maxLength = 6)
    @NotBlank
    @Size(min = 6, max = 6)
    private final String passportNumber;

    @Schema(name = "Кем выдан паспорт", minLength = 2, maxLength = 1000)
    @NotBlank
    @Size(min = 2, max = 1000)
    private final String passportIssued; // кем выдан

    @Schema(name = "Дата выдачи паспорта", minLength = 10, maxLength = 10)
    @NotBlank
    @Size(min = 10, max = 10)
    private final String passportDate;

    @Schema(name = "Код подразделения", minLength = 6, maxLength = 6)
    @NotBlank
    @Size(min = 6, max = 6)
    private final String passportKp; // код подразделения

    @Schema(name = "ID организации")
    @NotNull
    private final Long organizationId;

    @Schema(name = "Роль пользователя")
    @NotNull
    private final String role;

    @Schema(name = "Описание пользователя", minLength = 1, maxLength = 320)
    @NotBlank
    @Size(min = 1, max = 320)
    private String post;
}
