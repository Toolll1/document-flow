package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.services.DocumentProcessService;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.UserService;

@Component
@AllArgsConstructor
public class DocumentProcessSecurityService {
    DocumentProcessService documentProcessService;
    DocumentService documentService;
    UserService userService;

    public boolean isCanManageProcess(Long documentId, Long userId) {
        return documentService.findDocumentById(documentId).getOwnerId().equals(userId);
    }

    public boolean isHasAccessToProcess(Long processId, Long userId) {
        DocProcess docProcess = documentProcessService.findProcessById(processId);
        return docProcess.getSender().getId().equals(userId) || docProcess.getRecipientUserId().getId().equals(userId);
    }

    public boolean isRecipient(Long processId, Long userId) {
        DocProcess docProcess = documentProcessService.findProcessById(processId);
        return docProcess.getRecipientUserId().getId().equals(userId);
    }

    public boolean isProcessDone(Long processId) {
        DocProcess docProcess = documentProcessService.findProcessById(processId);
        return docProcess.getStatus().equals(DocProcessStatus.APPROVED) || docProcess.getStatus().equals(DocProcessStatus.REJECTED);
    }

    public boolean isMyCompany(Long documentId, Long userId) {
        return documentService.findDocumentById(documentId).getIdOrganization()
                .equals(userService.getUser(userId).getOrganization().getId());
    }

    public boolean isMyCompanyChanges(Long changesId, Long userId) {
        return isMyCompany(documentService.findDocChangesById(changesId).getDocumentId(), userId);
    }

    public boolean isSameCompany(Long creatorId, Long userId) {
        return userService.getUser(creatorId).getOrganization().equals(userService.getUser(userId).getOrganization());
    }

    public boolean isMyCompanyProcess(Long processId, Long userId) {
        return isMyCompany(documentProcessService.findProcessById(processId).getDocument().getId(), userId);
    }
}
