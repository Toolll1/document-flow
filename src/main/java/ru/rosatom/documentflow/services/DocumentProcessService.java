package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;

public interface DocumentProcessService {

    DocProcess createNewProcess(Long documentId, Long recipientId);

    void sendToApprove(Long processId);

    DocProcess findProcessById(Long processId);

    void approve(Long processId);
}
