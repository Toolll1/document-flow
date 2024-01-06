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
@Schema(description = "Паспорт пользователя")
public class UserPassportDto {
    @Schema(description = "Серия паспорта", minLength = 4, maxLength = 4, requiredMode = REQUIRED)
    private final String passportSeries;

    @Schema(description = "Номер паспорта", minLength = 6, maxLength = 6, requiredMode = REQUIRED)
    private final String passportNumber;

    @Schema(description = "Кем выдан паспорт", minLength = 2, maxLength = 1000, requiredMode = REQUIRED)
    private final String passportIssued; // кем выдан

    @Schema(description = "Дата выдачи паспорта", requiredMode = REQUIRED)
    private final LocalDate passportDate;

    @Schema(description = "Код подразделения", minLength = 6, maxLength = 6, requiredMode = REQUIRED)
    private final String passportKp; // код подразделения

}
