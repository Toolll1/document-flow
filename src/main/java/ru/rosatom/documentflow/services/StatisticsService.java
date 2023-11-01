package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocStatisticDTO;

public interface StatisticsService {
    DocStatisticDTO getCount();

    DocStatisticDTO getCountByStatus(String stringStatus);
}
