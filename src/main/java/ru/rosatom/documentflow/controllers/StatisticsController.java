package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/statics")
public class StatisticsController {

    private final UserService userService;
    private final UserOrganizationService userOrganizationService;

    @GetMapping("/userAndOrganization")
    public StatisticUsersAndOrg statisticsUserAndOrganization(){
        int countUser = userService.getAllUsers().size();
        int countOrganization = userOrganizationService.getAllOrganizations().size();
        log.info("Count of users: {} , count of organization: {}.", countUser ,countOrganization );
        return new StatisticUsersAndOrg(countUser, countOrganization);
    }
}
