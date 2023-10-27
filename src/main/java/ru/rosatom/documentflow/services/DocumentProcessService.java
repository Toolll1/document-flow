package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;

public interface DocumentProcessService {

    DocProcess createNewProcess(Long documentId, Long recipientId);

    void sendToApprove(ProcessUpdateRequest processUpdateRequest);

    DocProcess findProcessById(Long processId);

    void approve(ProcessUpdateRequest processUpdateRequest);
}
