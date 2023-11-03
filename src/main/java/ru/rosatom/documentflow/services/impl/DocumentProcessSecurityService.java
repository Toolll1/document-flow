package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.services.DocumentProcessService;
import ru.rosatom.documentflow.services.DocumentService;

@Component
@AllArgsConstructor
public class DocumentProcessSecurityService {
    DocumentProcessService documentProcessService;
    DocumentService documentService;

    public boolean isCanManageProcess(Long documentId, Long userId) {
        return documentService.findDocumentById(documentId).getOwnerId().equals(userId);
    }

    public boolean isHasAccess(Long processId, Long userId) {
        DocProcess docProcess = documentProcessService.findProcessById(processId);
        return docProcess.getSender().getId().equals(userId) || docProcess.getRecipient().getId().equals(userId);
    }

    public boolean isRecipient(Long processId, Long userId) {
        DocProcess docProcess = documentProcessService.findProcessById(processId);
        return docProcess.getRecipient().getId().equals(userId);
    }

    public boolean isProcessDone(Long processId) {
        DocProcess docProcess = documentProcessService.findProcessById(processId);
        return docProcess.getStatus().equals(DocProcessStatus.APPROVED) || docProcess.getStatus().equals(DocProcessStatus.REJECTED);
    }


}
