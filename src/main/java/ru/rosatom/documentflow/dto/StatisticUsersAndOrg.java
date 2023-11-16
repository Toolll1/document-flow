package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "Статистика пользователя и организации")
public class StatisticUsersAndOrg {
    @Schema(name = "Количество пользователей")
    int countUser;

    @Schema(name = "Количество организаций")
    int countOrganization;

    public StatisticUsersAndOrg(int countUser, int countOrganization) {
        this.countUser = countUser;
        this.countOrganization = countOrganization;
    }
}
