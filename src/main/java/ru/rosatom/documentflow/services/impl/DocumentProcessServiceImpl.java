package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.exceptions.IllegalProcessStatusException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocProcessRepository;
import ru.rosatom.documentflow.services.DocumentProcessService;

import java.util.*;
import java.util.stream.Collectors;

import static ru.rosatom.documentflow.models.DocProcessStatus.*;

@Component
@AllArgsConstructor
public class DocumentProcessServiceImpl implements DocumentProcessService {

    private final static String EMPTY_COMMENT = "";
    private final static Map<DocProcessStatus, List<DocProcessStatus>> ALLOWED_STATUS_CHANGES = Map.of(
            DocProcessStatus.NEW, List.of(DocProcessStatus.WAITING_FOR_APPROVE),
            DocProcessStatus.WAITING_FOR_APPROVE, List.of(DocProcessStatus.APPROVED, DocProcessStatus.REJECTED
                    , DocProcessStatus.CORRECTING, DocProcessStatus.DELEGATED),
            DocProcessStatus.CORRECTING, List.of(DocProcessStatus.WAITING_FOR_APPROVE),
            DocProcessStatus.APPROVED, List.of(),
            DocProcessStatus.REJECTED, List.of(),
            DocProcessStatus.DELEGATED, List.of()
    );
    private final DocumentServiceImpl documentService;
    private final UserServiceImpl userService;
    private final DocProcessRepository docProcessRepository;


    /**
     * Создает новый процесс согласования документа. Статус процесса - NEW
     *
     * @param documentId  - id документа
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
     *
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
     *
     * @param processUpdateRequest - запрос на обновление процесса
     */
    @Override
    public void approve(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.APPROVED);
        docProcess.setStatus(DocProcessStatus.APPROVED);
        docProcessRepository.save(docProcess);
        finalStatusUpdate(docProcess.getDocument().getId());
    }

    /**
     * Отклонить документ. Статус процесса - REJECTED
     *
     * @param processUpdateRequest
     */
    @Override
    public void reject(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.REJECTED);
        docProcess.setStatus(DocProcessStatus.REJECTED);
        docProcessRepository.save(docProcess);
        finalStatusUpdate(docProcess.getDocument().getId());
    }

    /**
     * Отправить на доработку. Статус процесса - CORRECTING
     *
     * @param processUpdateRequest
     */
    @Override
    public void sendToCorrection(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.CORRECTING);
        docProcess.setStatus(DocProcessStatus.CORRECTING);
        docProcessRepository.save(docProcess);
    }

    /**
     * Удалить запрос на согласование. Запрос может быть удален только если он не согласован или не отклонен
     *
     * @param processId
     */
    @Override
    public void deleteProcess(Long processId) {
        docProcessRepository.deleteById(processId);
    }


    private DocProcess getProcessAndApplyRequest(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = findProcessById(processUpdateRequest.getProcessId());
        docProcess.setComment(Objects.requireNonNullElse(processUpdateRequest.getComment(), docProcess.getComment()));
        return docProcess;
    }

    /**
     * Найти процесс согласования по id
     *
     * @param processId
     * @return DocProcess - процесс согласования
     */
    @Override
    public DocProcess findProcessById(Long processId) {
        return docProcessRepository.findById(processId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Процесс с id %d не найден", processId)));
    }

    /**
     * Найти все процессы согласования по id документа
     *
     * @param documentId
     * @return Collection<DocProcess> - коллекция процессов согласования
     */
    @Override
    public Collection<DocProcess> findProcessesByDocumentId(Long documentId) {
        return docProcessRepository.findAllByDocumentId(documentId);
    }

    private void finalStatusUpdate (Long documentId) {
        List<DocProcessStatus> processes = findProcessesByDocumentId(documentId)
                .stream()
                .map(DocProcess::getStatus)
                .collect(Collectors.toList());
        if (!List.of(APPROVED, REJECTED).containsAll(processes)) {
            return;
        }
        Document document = documentService.findDocumentById(documentId);
        AgreementType agreementType = document.getDocType().getAgreementType();
        switch (agreementType) {
            case EVERYONE:
                if (processes.contains(REJECTED)) {
                    documentService.updateFinalStatus(document, REJECTED);
                } else {
                    documentService.updateFinalStatus(document, APPROVED);
                }
                break;
            case ANYONE:
                if (processes.contains(APPROVED)) {
                    documentService.updateFinalStatus(document, APPROVED);
                } else {
                    documentService.updateFinalStatus(document, REJECTED);
                }
                break;
            case QUORUM:
                Map<DocProcessStatus, Integer> voteCount = new HashMap<>();
                for (DocProcessStatus vote : processes) {
                    if (!vote.equals(DELEGATED)) {
                        if (voteCount.containsKey(vote)) {
                            voteCount.put(vote, voteCount.get(vote) + 1);
                        } else {
                            voteCount.put(vote, 1);
                        }
                    }
                }
                int maxVotes = 0;
                int secondVotes = 0;
                DocProcessStatus finalStatus = WAITING_FOR_APPROVE;
                for (DocProcessStatus vote : voteCount.keySet()) {
                    int count = voteCount.get(vote);
                    if (count >= maxVotes) {
                        secondVotes = maxVotes;
                        maxVotes = count;
                        finalStatus = vote;
                    }
                }
                if (maxVotes != secondVotes) {
                    documentService.updateFinalStatus(document, finalStatus);
                }
        }
    }

    /**
     * Делегировать согласование другому пользователю. Статус процесса - DELEGATED
     *
     * @param processUpdateRequest - запрос на обновление процесса
     * @param recipientId - id получателя, которому делегировано согласование
     */
    @Override
    public DocProcess delegateToOtherUser(ProcessUpdateRequest processUpdateRequest, Long recipientId) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        docProcess.setStatus(DELEGATED);
        docProcessRepository.save(docProcess);
        return createNewProcess(docProcess.getDocument().getId(), recipientId);
    }
}
