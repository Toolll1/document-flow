package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.dto.UserRatingDto;

import java.util.List;

public interface StatisticsService {
    DocStatisticDTO getCount();

    DocStatisticDTO getCountByStatus(String stringStatus);

    StatisticUsersAndOrg statisticsUserAndOrganization();

    List<UserRatingDto> getRatingAllUsersByOrgId(Long orgId);
}
