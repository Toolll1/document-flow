package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Данные для обновления пользователя")
public class UserUpdateDto {



    @Schema(description = "Фамилия", minLength = 2, maxLength = 100)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 100)
    private final String lastName;

    @Schema(description = "Имя", minLength = 2, maxLength = 100)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 100)
    private final String firstName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 100)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 100)
    private final String patronymic;

    @Schema(description = "Дата рождения", minLength = 10, maxLength = 10)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 10, max = 10)
    private final String dateOfBirth;

    @Schema(description = "Email", minLength = 6, maxLength = 320)
    @Nullable
    @Email
    @Size(min = 6, max = 320)
    private final String email;

    @Schema(description = "Номер телефона", minLength = 11, maxLength = 11)
    @Nullable
    @Pattern(regexp = "\\d+")
    @Size(min = 11, max = 11)
    private final String phone;

    @Schema(description = "Серия паспорта", minLength = 4, maxLength = 4)
    @Nullable
    @Pattern(regexp = "\\d+")
    @Size(min = 4, max = 4)
    private final String passportSeries;

    @Schema(description = "Номер паспорта", minLength = 6, maxLength = 6)
    @Nullable
    @Pattern(regexp = "\\d+")
    @Size(min = 6, max = 6)
    private final String passportNumber;

    @Schema(description = "Кем выдан паспорт", minLength = 2, maxLength = 1000)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 1000)
    private final String passportIssued; // кем выдан

    @Schema(description = "Дата выдачи паспорта", minLength = 10, maxLength = 10)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 10, max = 10)
    private final String passportDate;

    @Schema(description = "Код подразделения", minLength = 6, maxLength = 6)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 6, max = 6)
    private final String passportKp; // код подразделения

    @Schema(description = "ID организации")
    @Nullable
    private final Long organizationId;

    @Schema(description = "Роль пользователя")
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    private final String role;

    @Schema(description = "Описание пользователя", minLength = 1, maxLength = 320)
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 1, max = 320)
    private String post;
}
