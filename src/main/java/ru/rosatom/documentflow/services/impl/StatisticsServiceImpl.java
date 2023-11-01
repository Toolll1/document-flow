package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.StatisticsService;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private DocumentService documentService;

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
}
