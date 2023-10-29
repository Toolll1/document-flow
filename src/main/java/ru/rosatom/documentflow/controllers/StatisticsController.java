package ru.rosatom.documentflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.DocStatisticDTO;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.services.DocumentProcessService;
import ru.rosatom.documentflow.services.DocumentService;

@RestController
@RequestMapping("/statistic/documents")
public class StatisticsController {

    private DocumentService documentService;
    private DocumentProcessService processService;

    @Autowired
    public StatisticsController(DocumentService documentService, DocumentProcessService processService) {
        this.documentService = documentService;
        this.processService = processService;
    }

    @GetMapping("/getCount")
    public DocStatisticDTO getCount() {
        return new DocStatisticDTO(documentService
                .getAllDocuments()
                .size()
        );
    }

    @GetMapping("getCountByStatus/{status}")
    public DocStatisticDTO getCountByStatus(@PathVariable String stringStatus) {
        DocProcessStatus status = null;
        if ("new".equalsIgnoreCase(stringStatus)) {
            status = DocProcessStatus.NEW;
        } else if ("waiting_for_approve".equalsIgnoreCase(stringStatus)) {
            status = DocProcessStatus.WAITING_FOR_APPROVE;
        } else if ("approved".equalsIgnoreCase(stringStatus)) {
            status = DocProcessStatus.APPROVED;
        } else if ("rejected".equalsIgnoreCase(stringStatus)) {
            status = DocProcessStatus.REJECTED;
        } else if ("correcting".equalsIgnoreCase(stringStatus)) {
            status = DocProcessStatus.CORRECTING;
        }
        return new DocStatisticDTO(processService
                .findDocumentsByProcessStatus(status)
                .size()
        );
    }
}
