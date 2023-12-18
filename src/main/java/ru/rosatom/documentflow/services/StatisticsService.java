package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrgDto;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

public interface StatisticsService {
    DocStatisticDTO getCount(User user);

    DocStatisticDTO getCountByStatus(String stringStatus, User user);

    StatisticUsersAndOrgDto statisticsUserAndOrganization(User user);

    List<UserRatingDto> getRatingAllUsersByOrgId(Long orgId, User user);

    List<UserOrganization> getAllActiveOrganization();
}
