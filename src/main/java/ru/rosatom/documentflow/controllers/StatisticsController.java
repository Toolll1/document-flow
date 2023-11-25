package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.*;
import ru.rosatom.documentflow.services.StatisticsService;
import ru.rosatom.documentflow.services.UserOrganizationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Статистика")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper;


    @Operation(summary = "Получить общее кол-во документов")
    @GetMapping("/documents/getCount")
    @SecurityRequirement(name = "JWT")
    public DocStatisticDTO getCount() {
        return statisticsService.getCount();
    }

    @Operation(summary = "Получить кол-во документов со статусом")
    @GetMapping("/documents/getCountByStatus/{status}")
    @SecurityRequirement(name = "JWT")
    public DocStatisticDTO getCountByStatus(
            @PathVariable @Parameter(description = "Наименование статуса") String status) {
        return statisticsService.getCountByStatus(status);
    }

    @Operation(summary = "Получить кол-во пользователей")
    @GetMapping("/users/count")
    @SecurityRequirement(name = "JWT")
    public CountUsersDto countUsers() {
        return modelMapper.map(statisticsService.statisticsUserAndOrganization(), CountUsersDto.class);
    }

    @Operation(summary = "Получить кол-во организаций")
    @GetMapping("/organisation/count")
    @SecurityRequirement(name = "JWT")
    public CountOrgDto countOrg() {
        return modelMapper.map(statisticsService.statisticsUserAndOrganization(), CountOrgDto.class);
    }

    private final UserOrganizationService organizationService;

    @Operation(summary = "Получить рейтинг активных пользователей по организации")
    @GetMapping("/userRating/{orgId}")
    @SecurityRequirement(name = "JWT")
    public List<UserRatingDto> getRating(
            @PathVariable @Parameter(description = "ID организации") Long orgId) {
        List<UserRatingDto> userRatingDtos = statisticsService.getRatingAllUsersByOrgId(orgId);
        return userRatingDtos;
    }

    @Operation(summary = "Получить список самых активных организаций")
    @GetMapping("/getActiveOrganization")
    @SecurityRequirement(name = "JWT")
    public List<OrgDto> getActiveOrganization() {
        return statisticsService.getAllActiveOrganization().stream()
                .map(o -> modelMapper.map(o, OrgDto.class))
                .collect(Collectors.toList());
    }

}