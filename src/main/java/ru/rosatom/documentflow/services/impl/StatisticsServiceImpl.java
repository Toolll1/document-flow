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
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.StatisticsService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private DocumentService documentService;
    private UserService userService;
    private UserOrganizationService userOrganizationService;
    private UserRepository userRepository;


    /**
     * Получить общее кол-во документов, при запросе от ADMIN поиск будет проходить по всей базе,
     * для остальных ролей поиск внутри своей компании.
     *
     * @param user - пользователь отправивший запрос
     * @return DocStatisticDTO - общее кол-во документов
     */
    @Override
    public DocStatisticDTO getCount(User user) {
        DocStatisticDTO statisticDTO;
        if (user.isAdmin()) {
            statisticDTO = new DocStatisticDTO(documentService.getAllDocuments().size());
        } else {
            statisticDTO = new DocStatisticDTO(documentService.findDocuments(
                            user.getId(),
                            new DocParams(), PageRequest.of(0, Integer.MAX_VALUE))
                    .stream()
                    .collect(Collectors.toSet())
                    .size());
        }
        return statisticDTO;
    }

    /**
     * Получить кол-во документов со статусом, при запросе от ADMIN поиск будет проходить по всей базе,
     * для остальных ролей поиск внутри своей компании.
     *
     * @param user - пользователь отправивший запрос
     * @param stringStatus - статус
     * @return DocStatisticDTO - кол-во документов со статусом
     */
    @Override
    public DocStatisticDTO getCountByStatus(String stringStatus, User user) {
        DocProcessStatus status = DocProcessStatus.valueOf(stringStatus);
        DocStatisticDTO statisticDTO;
        if (user.isAdmin()) {
            statisticDTO = new DocStatisticDTO(documentService.findDocumentsByProcessStatus(status).size());
        } else {
            statisticDTO = new DocStatisticDTO(documentService.findDocumentsByProcessStatusAndIdOrganization(status, user.getOrganization().getId()).size());
        }
        return statisticDTO;
    }

    /**
     * Получить кол-во пользователей, при запросе от ADMIN поиск будет проходить по всей базе,
     * для остальных ролей поиск внутри своей компании.
     *
     * @param user - пользователь отправивший запрос
     * @return StatisticUsersAndOrgDto - кол-во пользователей и организаций
     */
    @Override
    public StatisticUsersAndOrgDto statisticsUserAndOrganization(User user) {
        int countUser;
        int countOrganization;
        if (user.isAdmin()) {
            countUser = userService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE)).stream().collect(Collectors.toSet()).size();
            countOrganization = userOrganizationService.getAllOrganizations(PageRequest.of(0, Integer.MAX_VALUE))
                    .stream()
                    .collect(Collectors.toSet())
                    .size();
        } else {
            countUser = userService.findAllByOrganizationId(user.getOrganization().getId()).size();
            countOrganization = 1;
        }
        return new StatisticUsersAndOrgDto(countUser, countOrganization);
    }

    /**
     * Получить рейтинг активных пользователей по организации, при запросе от ADMIN по указанной компании,
     * для остальных ролей поиск внутри своей компании.
     *
     * @param user - пользователь отправивший запрос
     * @param orgId - ID организации
     * @return UserRatingDto - Получить рейтинг активных пользователей по организации
     */
    @Override
    public List<UserRatingDto> getRatingAllUsersByOrgId(Long orgId, User user) {
        List<UserRatingDto> rating;
        if (user.isAdmin()) {
            rating = userRepository.findRatingForAllUsersByOrganizationId(orgId);
        } else {
            rating = userRepository.findRatingForAllUsersByOrganizationId(user.getOrganization().getId());
        }
        return rating;
    }

    @Override
    public List<UserOrganization> getAllActiveOrganization() {
        return userOrganizationService.getAllActiveOrganization();
    }
}
