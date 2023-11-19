package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Schema(name = "Ответ на получение пользователя")
public class UserReplyDto {

    @Schema(name = "ID пользователя")
    private final Long id;

    @Schema(name = "ФИО")
    private final String fullName;

    @Schema(name = "Дата рождения")
    private final String dateOfBirth;

    @Schema(name = "Email")
    private final String email;

    @Schema(name = "Телефон")
    private final String phone;

    @Schema(name = "Описание")
    private final String post;

    @Schema(name = "Роль")
    private final String role;

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

    @Schema(name = "Название организации")
    private String nameOrganization;

    @Schema(name = "ИНН организации")
    private String innOrganization;

}
