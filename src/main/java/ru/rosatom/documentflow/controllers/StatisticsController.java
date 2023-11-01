package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.services.StatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic/documents")
public class StatisticsController {
    private StatisticsService statisticsService;

    @GetMapping("/getCount")
    public DocStatisticDTO getCount() {
        return statisticsService.getCount();
    }

    @GetMapping("getCountByStatus/{status}")
    public DocStatisticDTO getCountByStatus(@PathVariable String stringStatus) {
        return statisticsService.getCountByStatus(stringStatus);
    }
}
