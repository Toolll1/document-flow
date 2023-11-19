package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Статистика пользователя и организации")
public class StatisticUsersAndOrgDto {
    @Schema(description = "Количество пользователей")
    private final int countUser;

    @Schema(description = "Количество организаций")
    private final int countOrganization;

}
