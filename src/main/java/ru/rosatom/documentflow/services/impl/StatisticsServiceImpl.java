package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.StatisticsService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private DocumentService documentService;
    private UserService userService;
    private UserOrganizationService userOrganizationService;

    @Override
    public DocStatisticDTO getCount() {
        return new DocStatisticDTO(documentService
                .getAllDocuments()
                .size()
        );
    }

    @Override
    public DocStatisticDTO getCountByStatus(String stringStatus) {
        DocProcessStatus status = DocProcessStatus.valueOf(stringStatus);
        return new DocStatisticDTO(documentService
                .findDocumentsByProcessStatus(status)
                .size()
        );
    }

    @Override
    public StatisticUsersAndOrg statisticsUserAndOrganization() {
        int countUser = userService.getAllUsers().size();
        int countOrganization = userOrganizationService.getAllOrganizations().size();
        return new StatisticUsersAndOrg(countUser, countOrganization);
    }
}
