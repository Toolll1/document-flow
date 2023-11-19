package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Статистика пользователя и организации")
public class StatisticUsersAndOrgDto {
    @Schema(name = "Количество пользователей")
    private final int countUser;

    @Schema(name = "Количество организаций")
    private final int countOrganization;

}
