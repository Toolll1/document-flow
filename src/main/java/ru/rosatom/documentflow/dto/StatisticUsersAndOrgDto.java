package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Schema(description = "Статистика пользователя и организации")
public class StatisticUsersAndOrgDto {
    @Schema(description = "Количество пользователей", requiredMode = REQUIRED)
    private final int countUser;

    @Schema(description = "Количество организаций", requiredMode = REQUIRED)
    private final int countOrganization;

}
