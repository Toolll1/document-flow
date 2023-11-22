package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Schema(description = "Паспорт пользователя")
public class UserPassportDto {
    @Schema(description = "Серия паспорта", minLength = 4, maxLength = 4)
    private final String passportSeries;

    @Schema(description = "Номер паспорта", minLength = 6, maxLength = 6)
    private final String passportNumber;

    @Schema(description = "Кем выдан паспорт", minLength = 2, maxLength = 1000)
    private final String passportIssued; // кем выдан

    @Schema(description = "Дата выдачи паспорта", minLength = 10, maxLength = 10)
    private final String passportDate;

    @Schema(description = "Код подразделения", minLength = 6, maxLength = 6)
    private final String passportKp; // код подразделения

}
