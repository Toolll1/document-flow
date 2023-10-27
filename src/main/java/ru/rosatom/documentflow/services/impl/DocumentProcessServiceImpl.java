package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.exceptions.IllegalProcessStatusException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocProcessRepository;
import ru.rosatom.documentflow.services.DocumentProcessService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor
public class DocumentProcessServiceImpl implements DocumentProcessService {

    private final DocumentServiceImpl documentService;
    private final UserServiceImpl userService;

    private final static String EMPTY_COMMENT = "";

    private final static Map<DocProcessStatus, List<DocProcessStatus>> ALLOWED_STATUS_CHANGES = Map.of(
            DocProcessStatus.NEW, List.of(DocProcessStatus.WAITING_FOR_APPROVE),
            DocProcessStatus.WAITING_FOR_APPROVE, List.of(DocProcessStatus.APPROVED, DocProcessStatus.REJECTED, DocProcessStatus.CORRECTING),
            DocProcessStatus.CORRECTING, List.of(DocProcessStatus.WAITING_FOR_APPROVE),
            DocProcessStatus.APPROVED, List.of(),
            DocProcessStatus.REJECTED, List.of()
    );

    private final DocProcessRepository docProcessRepository;


    /**
     * Создает новый процесс согласования документа. Статус процесса - NEW
     * @param documentId - id документа
     * @param recipientId - id получателя
     * @return DocProcess - процесс согласования
     */
    @Override
    public DocProcess createNewProcess(Long documentId, Long recipientId) {
        Document document = documentService.findDocumentById(documentId);
        User recipient = userService.getUser(recipientId);
        User sender = userService.getUser(document.getOwnerId());
        DocProcess docProcess = DocProcess.builder()
                .document(document)
                .recipient(recipient)
                .sender(sender)
                .status(DocProcessStatus.NEW)
                .comment(EMPTY_COMMENT)
                .build();
        return docProcessRepository.save(docProcess);
    }

    /**
     * Отправляет процесс согласования на согласование. Статус процесса - WAITING_FOR_APPROVE
     * @param processUpdateRequest - запрос на обновление процесса
     */
    @Override
    public void sendToApprove(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.WAITING_FOR_APPROVE);
        docProcess.setStatus(DocProcessStatus.WAITING_FOR_APPROVE);
        docProcessRepository.save(docProcess);
    }

    private void throwIfStatusNotCorrect(DocProcess docProcess, DocProcessStatus attemptStatus) {
        if (!ALLOWED_STATUS_CHANGES.get(docProcess.getStatus()).contains(attemptStatus)) {
            throw new IllegalProcessStatusException(docProcess, attemptStatus);
        }
    }

    /**
     * Согласовать документ. Статус процесса - APPROVED
     * @param processUpdateRequest - запрос на обновление процесса
     */
    @Override
    public void approve(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.APPROVED);
        docProcess.setStatus(DocProcessStatus.APPROVED);
        docProcessRepository.save(docProcess);
    }

    @Override
    public void reject(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.REJECTED);
        docProcess.setStatus(DocProcessStatus.REJECTED);
        docProcessRepository.save(docProcess);
    }

    @Override
    public void sendToCorrection(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.CORRECTING);
        docProcess.setStatus(DocProcessStatus.CORRECTING);
        docProcessRepository.save(docProcess);
    }

    @Override
    public void deleteProcess(Long processId) {
        docProcessRepository.deleteById(processId);
    }


    private DocProcess getProcessAndApplyRequest(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = findProcessById(processUpdateRequest.getProcessId());
        docProcess.setComment(Objects.requireNonNullElse(processUpdateRequest.getComment(), docProcess.getComment()));
        return docProcess;
    }

    @Override
    public DocProcess findProcessById(Long processId) {
        return docProcessRepository.findById(processId)
                .orElseThrow(() -> new ObjectNotFoundException("Процесс с id %d не найден".formatted(processId)));
    }

    @Override
    public Collection<DocProcess> findProcessesByDocumentId(Long documentId) {
        return docProcessRepository.findAllByDocumentId(documentId);
    }

}
