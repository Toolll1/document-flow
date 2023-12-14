package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.models.*;

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

    DocProcessComment createComment(String text, User user, DocProcess docProcess);
}
