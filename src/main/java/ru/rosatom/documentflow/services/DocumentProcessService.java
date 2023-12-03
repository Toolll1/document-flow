package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessComment;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;
import ru.rosatom.documentflow.models.User;

import java.util.Collection;
import java.util.List;

public interface DocumentProcessService {

    DocProcess createNewProcess(Long documentId, Long recipientId);

    void sendToApprove(ProcessUpdateRequest processUpdateRequest);

    DocProcess findProcessById(Long processId);

    Collection<DocProcess> findProcessesByDocumentId(Long documentId);
    List<DocProcess> getIncomingProcessesByUserId(Long userId);

    List<DocProcess> getOutgoingProcessesByUserId(Long userId);

    void approve(ProcessUpdateRequest processUpdateRequest);

    void reject(ProcessUpdateRequest processUpdateRequest);

    void sendToCorrection(ProcessUpdateRequest processUpdateRequest);

    void deleteProcess(Long processId);

    DocProcess delegateToOtherUser(ProcessUpdateRequest processUpdateRequest, Long recipientId);

    DocProcessComment createComment(String text, User user);

}
