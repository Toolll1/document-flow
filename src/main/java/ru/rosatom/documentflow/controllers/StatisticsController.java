package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.services.StatisticsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
@Tag(name = "Статистика", description = "Управляет статистикой")
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
    public StatisticUsersAndOrg statisticsUserAndOrganization() {
        return statisticsService.statisticsUserAndOrganization();
    }

    @GetMapping("/userRating/{orgId}")
    public List<UserRatingDto> getRating(@PathVariable Long orgId) {
        return statisticsService.getRatingAllUsersByOrgId(orgId);
    }
    @GetMapping("/getActiveOrganization")
    public List<UserOrganization> getActiveOrganization(){
        return statisticsService.getAllActiveOrganization();
    }
}
