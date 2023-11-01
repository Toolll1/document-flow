package ru.rosatom.documentflow.dto;

public class StatisticUsersAndOrg {
    int countUser;
    int countOrganization;

    public StatisticUsersAndOrg(int countUser, int countOrganization) {
        this.countUser = countUser;
        this.countOrganization = countOrganization;
    }
}
