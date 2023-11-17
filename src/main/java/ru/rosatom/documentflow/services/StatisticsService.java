package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.ReasonChangesInDocumentDto;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;

public interface StatisticsService {
    DocStatisticDTO getCount();

    DocStatisticDTO getCountByStatus(String stringStatus);

    StatisticUsersAndOrg statisticsUserAndOrganization();

    ReasonChangesInDocumentDto reasonChanges(Long id);
}
