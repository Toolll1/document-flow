package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

public interface StatisticsService {
    DocStatisticDTO getCount();

    DocStatisticDTO getCountByStatus(String stringStatus);

    StatisticUsersAndOrg statisticsUserAndOrganization();

    List<UserOrganization> getAllActiveOrganization();
}
