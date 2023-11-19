package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrgDto;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

public interface StatisticsService {
    DocStatisticDTO getCount();

    DocStatisticDTO getCountByStatus(String stringStatus);

    StatisticUsersAndOrgDto statisticsUserAndOrganization();

    List<UserRatingDto> getRatingAllUsersByOrgId(Long orgId);

    List<UserOrganization> getAllActiveOrganization();
}
