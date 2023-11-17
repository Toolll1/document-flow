package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.ReasonChangesInDocumentDto;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.services.StatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/documents/getCount")
    public DocStatisticDTO getCount() {
        return statisticsService.getCount();
    }

    @GetMapping("/documents/getCountByStatus/{status}")
    public DocStatisticDTO getCountByStatus(@PathVariable String stringStatus) {
        return statisticsService.getCountByStatus(stringStatus);
    }

    @GetMapping("/userAndOrganisation")
    public StatisticUsersAndOrg statisticsUserAndOrganization(){
        return statisticsService.statisticsUserAndOrganization();
    }

    @GetMapping("/reasonsChanges/{id}")
    public ReasonChangesInDocumentDto reasonChangesInDocument(@PathVariable String id) {
        return statisticsService.reasonChanges(Long.parseLong(id));
    }
}
