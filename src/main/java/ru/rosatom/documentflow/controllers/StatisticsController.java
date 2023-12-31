package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.CountOrgDto;
import ru.rosatom.documentflow.dto.CountUsersDto;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.OrgDto;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.StatisticsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/statistic")
@Tag(name = "Статистика")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper;


    @Operation(summary = "Получить общее кол-во документов.",
    description = "при запросе от ADMIN поиск будет проходить по всей базе, для остальных ролей поиск внутри своей компании")
    @GetMapping("/documents/getCount")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('COMPANY_ADMIN')")
    public DocStatisticDTO getCount(@AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        return statisticsService.getCount(user);
    }

    @Operation(summary = "Получить кол-во документов со статусом",
            description = "При запросе от ADMIN поиск будет проходить по всей базе, для остальных ролей поиск внутри своей компании")
    @GetMapping("/documents/getCountByStatus/{status}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('COMPANY_ADMIN')")
    public DocStatisticDTO getCountByStatus(
            @PathVariable @Parameter(description = "Наименование статуса") String status,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        return statisticsService.getCountByStatus(status, user);
    }

    @Operation(summary = "Получить кол-во пользователей",
    description = "При запросе от ADMIN поиск будет проходить по всей базе, для остальных ролей поиск внутри своей компании")
    @GetMapping("/users/count")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('COMPANY_ADMIN')")
    public CountUsersDto countUsers(@AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        return modelMapper.map(statisticsService.statisticsUserAndOrganization(user), CountUsersDto.class);
    }

    @Operation(summary = "Получить кол-во организаций")
    @GetMapping("/organisation/count")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CountOrgDto countOrg(@AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        return modelMapper.map(statisticsService.statisticsUserAndOrganization(user), CountOrgDto.class);
    }

    @Operation(summary = "Получить рейтинг активных пользователей по организации.",
            description = "При запросе от ADMIN по указанной компании, для остальных ролей поиск внутри своей компании")
    @GetMapping("/userRating/{orgId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('COMPANY_ADMIN')")
    public List<UserRatingDto> getRating(
            @PathVariable @Parameter(description = "ID организации") Long orgId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        return statisticsService.getRatingAllUsersByOrgId(orgId, user);
    }

    @Operation(summary = "Получить список самых активных организаций")
    @GetMapping("/getActiveOrganization")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrgDto> getActiveOrganization() {
        return statisticsService.getAllActiveOrganization().stream()
                .map(o -> modelMapper.map(o, OrgDto.class))
                .collect(Collectors.toList());
    }
}