package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;

import java.util.Collection;
import java.util.List;

public interface DocumentProcessService {

    DocProcess createNewProcess(Long documentId, Long recipientId);

    void sendToApprove(ProcessUpdateRequest processUpdateRequest, String textComment);

    DocProcess findProcessById(Long processId);

    Collection<DocProcess> findProcessesByDocumentId(Long documentId);

    List<DocProcess> getIncomingProcessesByUserId(Long userId);

    List<DocProcess> getOutgoingProcessesByUserId(Long userId);

    void approve(ProcessUpdateRequest processUpdateRequest, String textComment);

    void reject(ProcessUpdateRequest processUpdateRequest, String textComment);

    void sendToCorrection(ProcessUpdateRequest processUpdateRequest, String textComment);

    void deleteProcess(Long processId);

    DocProcess delegateToOtherUser(ProcessUpdateRequest processUpdateRequest, Long recipientId);
}
