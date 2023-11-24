package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;

import javax.mail.MessagingException;
import java.util.Collection;
import java.util.List;

public interface DocumentProcessService {

    DocProcess createNewProcess(Long documentId, Long recipientId);

    void sendToApprove(ProcessUpdateRequest processUpdateRequest) throws MessagingException;

    DocProcess findProcessById(Long processId);

    Collection<DocProcess> findProcessesByDocumentId(Long documentId);

    List<DocProcess> getIncomingProcessesByUserId(Long userId);

    List<DocProcess> getOutgoingProcessesByUserId(Long userId);

    void approve(ProcessUpdateRequest processUpdateRequest) throws MessagingException;

    void reject(ProcessUpdateRequest processUpdateRequest) throws MessagingException;

    void sendToCorrection(ProcessUpdateRequest processUpdateRequest) throws MessagingException;

    void deleteProcess(Long processId);

    DocProcess delegateToOtherUser(ProcessUpdateRequest processUpdateRequest, Long recipientId);
}
