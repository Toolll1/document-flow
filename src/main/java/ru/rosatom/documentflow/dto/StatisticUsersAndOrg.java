package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(name = "Статистика пользователя и организации")
public class StatisticUsersAndOrg {

    @Schema(name = "Количество пользователей")
    int countUser;

    @Schema(name = "Количество организаций")
    int countOrganization;
}
