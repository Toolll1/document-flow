package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Статистика пользователя и организации")
public class StatisticUsersAndOrg {
    @Schema(description = "Количество пользователей")
    int countUser;

    @Schema(description = "Количество организаций")
    int countOrganization;

    public StatisticUsersAndOrg(int countUser, int countOrganization) {
        this.countUser = countUser;
        this.countOrganization = countOrganization;
    }
}
