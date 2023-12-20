package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.exceptions.IllegalProcessStatusException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.exceptions.UnprocessableEntityException;
import ru.rosatom.documentflow.kafka.Producer;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocProcessRepository;
import ru.rosatom.documentflow.services.*;

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
    private final DocumentService documentService;
    private final UserService userService;
    private final DocProcessRepository docProcessRepository;
    private final EmailService emailService;
    private final Producer producer;
    private final UserOrganizationService userOrganizationService;


    /**
     * Создает новый процесс согласования документа для указанной компании.
     * Статус процесса - NEW.
     *
     * @param documentId - id документа
     * @param companyId  - id компании получателя
     * @return DocProcess - процесс согласования
     */
    @Override
    public DocProcess createNewProcessToOtherCompany(Long documentId, Long companyId) {
        Document document = documentService.findDocumentById(documentId);
        UserOrganization recipientCompany = userOrganizationService.getOrganization(companyId);
        if (recipientCompany.getDefaultRecipient() == null)
            throw new UnprocessableEntityException(
                    String.format("The default recipient for company '%s' cannot be determined", recipientCompany.getName()));
        User sender = userService.getUser(document.getOwnerId());
        DocProcess docProcess = DocProcess.builder()
                .document(document)
                .sender(sender)
                .recipientUserId(userService.getUser(recipientCompany.getDefaultRecipient()))
                .recipientOrganizationId(recipientCompany)
                .status(DocProcessStatus.NEW)
                .comment(EMPTY_COMMENT)
                .build();
        return docProcessRepository.save(docProcess);
    }


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
                .recipientUserId(recipient)
                .sender(sender)
                .recipientOrganizationId(sender.getOrganization())
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
        emailService.sendDocProcessAgreementMessageForRecipient(docProcess);
        docProcessRepository.save(docProcess);
    }

    /**
     * Получить список процессов по id получателя
     *
     * @param userId - id получателя
     * @return Список процессов
     */
    public List<DocProcess> getIncomingProcessesByUserId(Long userId) {
        return docProcessRepository.findAllByRecipientUserId(userId)
                .stream()
                .filter(docProcess -> !docProcess.getStatus().equals(NEW))
                .collect(Collectors.toList());
    }

    /**
     * Получить список процессов по id отправителя
     *
     * @param userId - id отправителя
     * @return Список процессов
     */
    public List<DocProcess> getOutgoingProcessesByUserId(Long userId) {
        return docProcessRepository.findAllBySenderId(userId);
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
        emailService.sendDocProcessResultMessageForOwner(docProcess, MessagePattern.APPROVE);
        finalStatusUpdate(docProcess.getDocument().getId());
    }

    /**
     * Отклонить документ. Статус процесса - REJECTED
     *
     * @param processUpdateRequest - запрос на обновление процесса
     */
    @Override
    public void reject(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.REJECTED);
        docProcess.setStatus(DocProcessStatus.REJECTED);
        docProcessRepository.save(docProcess);
        emailService.sendDocProcessResultMessageForOwner(docProcess, MessagePattern.REJECT);
        finalStatusUpdate(docProcess.getDocument().getId());
    }

    /**
     * Отправить на доработку. Статус процесса - CORRECTING
     *
     * @param processUpdateRequest - запрос на обновление процесса
     */
    @Override
    public void sendToCorrection(ProcessUpdateRequest processUpdateRequest) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        throwIfStatusNotCorrect(docProcess, DocProcessStatus.CORRECTING);
        setStatusForAllProcessesExceptByDocument(docProcess.getDocument(), CORRECTING, List.of(CORRECTING, NEW));
        docProcessRepository.save(docProcess);
        emailService.sendDocProcessResultMessageForOwner(docProcess, MessagePattern.CORRECTING);
    }

    /**
     * Установить статус для всех процессов по документу кроме процессов со статусами из списка exceptStatuses
     *
     * @param document       - документ для которого нужно установить статус
     * @param newStatus      - новый статус
     * @param exceptStatuses - список статусов, которые нужно исключить
     */
    public void setStatusForAllProcessesExceptByDocument(Document document, DocProcessStatus newStatus, List<DocProcessStatus> exceptStatuses) {
        List<DocProcess> processes = docProcessRepository.findAllByDocumentId(document.getId());
        processes.stream()
                .filter(process -> !exceptStatuses.contains(process.getStatus()))
                .forEach(process -> process.setStatus(newStatus));
        docProcessRepository.saveAll(processes);
    }

    /**
     * Удалить запрос на согласование. Запрос может быть удален только если он не согласован или не отклонен
     *
     * @param processId - id процесса
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
     * @param processId - id процесса
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
     * @param documentId - id процесса
     * @return Collection<DocProcess> - коллекция процессов согласования
     */
    @Override
    public List<DocProcess> findProcessesByDocumentId(Long documentId) {
        return docProcessRepository.findAllByDocumentId(documentId);
    }

    private void finalStatusUpdate(Long documentId) {
        Collection<DocProcess> docProcess = findProcessesByDocumentId(documentId);
        List<DocProcessStatus> processes = docProcess
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
                    documentService.updateFinalStatus(document, REJECTED, null);
                } else {
                    documentService.updateFinalStatus(document, APPROVED, docProcess);
                }
                break;
            case ANYONE:
                if (processes.contains(APPROVED)) {
                    documentService.updateFinalStatus(document, APPROVED, docProcess);
                } else {
                    documentService.updateFinalStatus(document, REJECTED, null);
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

                    if (finalStatus.equals(APPROVED)) {
                        documentService.updateFinalStatus(document, finalStatus, docProcess);
                    } else {
                        documentService.updateFinalStatus(document, finalStatus, null);
                    }
                }
        }
        if (document.getFinalDocStatus().equals(APPROVED)) {
            producer.sendMessage("Документ с id- [" + documentId + "] переведен в статус: " + APPROVED);
        }
    }

    /**
     * Делегировать согласование другому пользователю. Статус процесса - DELEGATED
     *
     * @param processUpdateRequest - запрос на обновление процесса
     * @param recipientId          - id получателя, которому делегировано согласование
     */
    @Override
    public DocProcess delegateToOtherUser(ProcessUpdateRequest processUpdateRequest, Long recipientId) {
        DocProcess docProcess = getProcessAndApplyRequest(processUpdateRequest);
        docProcess.setStatus(DELEGATED);
        docProcessRepository.save(docProcess);
        return createNewProcess(docProcess.getDocument().getId(), recipientId);
    }
}
