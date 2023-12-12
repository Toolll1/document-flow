package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocParams;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrgDto;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserRole;
import ru.rosatom.documentflow.repositories.UserOrganizationRepository;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.StatisticsService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private DocumentService documentService;
    private UserService userService;
    private UserOrganizationService userOrganizationService;
    private UserOrganizationRepository userOrganizationRepository;
    private UserRepository userRepository;

    @Override
    public DocStatisticDTO getCount(User user) {
        DocStatisticDTO statisticDTO = new DocStatisticDTO(0);
        if (user.getRole() == UserRole.ADMIN) {
            statisticDTO = new DocStatisticDTO(documentService.getAllDocuments().size());
        } else if (user.getRole() == UserRole.ADMINCOMPANY) {
            statisticDTO = new DocStatisticDTO(documentService.findDocuments(
                    user.getId(),
                    new DocParams(), PageRequest.of(0, Integer.MAX_VALUE))
                    .stream()
                    .collect(Collectors.toSet())
                    .size());
        }
        return statisticDTO;
    }

    @Override
    public DocStatisticDTO getCountByStatus(String stringStatus, User user) {
        DocProcessStatus status = DocProcessStatus.valueOf(stringStatus);
        DocStatisticDTO statisticDTO = new DocStatisticDTO(0);
        if (user.getRole() == UserRole.ADMIN) {
            statisticDTO = new DocStatisticDTO(documentService.findDocumentsByProcessStatus(status).size());
        } else if (user.getRole() == UserRole.ADMINCOMPANY) {
            statisticDTO = new DocStatisticDTO(documentService.findDocumentsByProcessStatusAndIdOrganization(status, user.getOrganization().getId()).size());
        }
        return statisticDTO;
    }

    @Override
    public StatisticUsersAndOrgDto statisticsUserAndOrganization(User user) {
        int countUser = 0;
        int countOrganization = 0;
        if (user.getRole() == UserRole.ADMIN) {
            countUser = userService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE)).stream().collect(Collectors.toSet()).size();
            countOrganization = userOrganizationService.getAllOrganizations(PageRequest.of(0, Integer.MAX_VALUE))
                    .stream()
                    .collect(Collectors.toSet())
                    .size();
        } else if (user.getRole() == UserRole.ADMINCOMPANY) {
            countUser = userService.findAllByOrganizationId(user.getOrganization().getId()).size();
            countOrganization = 1;
        }
        return new StatisticUsersAndOrgDto(countUser, countOrganization);
    }

    @Override
    public List<UserRatingDto> getRatingAllUsersByOrgId(Long orgId, User user) {
        List<UserRatingDto> rating = new ArrayList<>();
        if (user.getRole() == UserRole.ADMIN) {
            rating = userRepository.findRatingForAllUsersByOrganizationId(orgId);
        } else if (user.getRole() == UserRole.ADMINCOMPANY) {
            rating = userRepository.findRatingForAllUsersByOrganizationId(user.getOrganization().getId());
        }
        return rating;
    }

    @Override
    public List<UserOrganization> getAllActiveOrganization() {
        return userOrganizationService.getAllActiveOrganization();
    }
}
