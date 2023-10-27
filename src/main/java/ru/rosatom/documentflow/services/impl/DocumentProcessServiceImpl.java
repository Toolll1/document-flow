package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.repositories.DocProcessRepository;
import ru.rosatom.documentflow.services.DocumentProcessService;

@Component
@AllArgsConstructor
public class DocumentProcessServiceImpl implements DocumentProcessService {

    private final DocumentServiceImpl documentService;
    private final UserServiceImpl userService;

    private final static String EMPTY_COMMENT = "";

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
     * @param processId - id процесса
     * @return DocProcess - процесс согласования с новым статусом
     */
    @Override
    public void sendToApprove(Long processId) {
        DocProcess docProcess = findProcessById(processId);
        docProcess.setStatus(DocProcessStatus.WAITING_FOR_APPROVE);
        docProcessRepository.save(docProcess);
    }

    /**
     * Согласовать документ. Статус процесса - APPROVED
     * @param processId - id процесса
     * @return DocProcess - процесс согласования с новым статусом
     */
    @Override
    public void approve(Long processId) {
        DocProcess docProcess = findProcessById(processId);
        docProcess.setStatus(DocProcessStatus.APPROVED);
        docProcessRepository.save(docProcess);
    }

    @Override
    public DocProcess findProcessById(Long processId) {
        return docProcessRepository.findById(processId)
                .orElseThrow(() -> new ObjectNotFoundException("Процесс с id %d не найден".formatted(processId)));
    }

}
