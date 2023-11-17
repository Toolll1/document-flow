package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.dto.ReasonChangesInDocumentDto;
import ru.rosatom.documentflow.dto.StatisticUsersAndOrg;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.services.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private DocumentService documentService;
    private UserService userService;
    private UserOrganizationService userOrganizationService;
    private DocumentProcessService documentProcessService;

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
    public StatisticUsersAndOrg statisticsUserAndOrganization(){
        int countUser = userService.getAllUsers().size();
        int countOrganization = userOrganizationService.getAllOrganizations().size();
        return new StatisticUsersAndOrg(countUser, countOrganization);
    }

    @Override
    public ReasonChangesInDocumentDto reasonChanges(Long id) {
        Collection<DocProcess> docProcess = documentProcessService.findProcessesByDocumentId(id);
        return ReasonChangesInDocumentDto.builder().documentChanges(
                docProcess.stream().map(DocProcess::getComment).collect(Collectors.toList())
        ).build();
    }
}
