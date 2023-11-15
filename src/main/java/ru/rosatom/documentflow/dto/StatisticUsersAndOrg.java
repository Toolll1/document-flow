package ru.rosatom.documentflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatisticUsersAndOrg {
    private int countUser;
    private int countOrganization;

    public StatisticUsersAndOrg(int countUser, int countOrganization) {
        this.countUser = countUser;
        this.countOrganization = countOrganization;
    }
}
