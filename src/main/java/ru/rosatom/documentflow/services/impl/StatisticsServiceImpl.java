package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrgDto;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.StatisticsService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private DocumentService documentService;
    private UserService userService;
    private UserOrganizationService userOrganizationService;
    private UserRepository userRepository;

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
    public StatisticUsersAndOrgDto statisticsUserAndOrganization() {
        int countUser = userService.getAllUsers().size();
        int countOrganization = userOrganizationService.getAllOrganizations().size();
        return new StatisticUsersAndOrgDto(countUser, countOrganization);
    }

    @Override
    public List<UserRatingDto> getRatingAllUsersByOrgId(Long orgId) {
        return userRepository.findRatingForAllUsersByOrganizationId(orgId);
    }

    @Override
    public List<UserOrganization> getAllActiveOrganization() {
        return userOrganizationService.getAllActiveOrganization();
    }
}
