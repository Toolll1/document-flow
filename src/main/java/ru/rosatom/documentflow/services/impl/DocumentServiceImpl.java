package ru.rosatom.documentflow.services.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.QDocument;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.repositories.DocChangesRepository;
import ru.rosatom.documentflow.repositories.DocumentRepository;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ru.rosatom.documentflow.adapters.CommonUtils.*;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    final DocumentRepository documentRepository;
    final DocChangesRepository docChangesRepository;
    final UserService userService;
    final UserOrganizationService userOrganizationService;

    @Override
    @Transactional
    public Document createDocument(Document document, Long userId) {
        userService.getUser(userId);
        userOrganizationService.getOrganization(document.getIdOrganization());
        return documentRepository.save(document);
    }

    @Override
    @Transactional
    public Document updateDocument(DocumentUpdateDto documentUpdateDto, Long id, Long userId) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден документ с id " + id));
        DocChanges docChanges = new DocChanges();
        docChanges.setDocumentId(id);
        docChanges.setDateChange(LocalDate.now());
        docChanges.setPreviousVersion(document.getDocumentPath());
        docChanges.setUserChangerId(userId);
        docChanges.setUserOwnerId(document.getOwnerId());
        StringBuilder sb = new StringBuilder();
        if (documentUpdateDto.getTitle() != null) {
            document.setTitle(documentUpdateDto.getTitle());
            sb.append(CHANGE_TITLE);
        }
        if (documentUpdateDto.getDate() != null) {
            document.setDate(documentUpdateDto.getDate());
            sb.append(CHANGE_DATE);
        }
        if (documentUpdateDto.getDocumentPath() != null) {
            document.setDocumentPath(documentUpdateDto.getDocumentPath());
            sb.append(CHANGE_DOCUMENT_PATH);
        }
        if (documentUpdateDto.getDocType() != null) {
            document.setDocType(documentUpdateDto.getDocType());
            sb.append(CHANGE_DOCUMENT_TYPE);
        }
        if (documentUpdateDto.getOwnerId() != null) {
            document.setOwnerId(documentUpdateDto.getOwnerId());
            docChanges.setUserOwnerId(documentUpdateDto.getOwnerId());
            sb.append(CHANGE_DOCUMENT_OWNER);
        }
        docChanges.setChanges(sb.toString());
        docChangesRepository.save(docChanges);
        return documentRepository.save(document);
    }

    @Override
    public Document findDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден документ с id " + documentId));
    }

    @Override
    public List<Document> getAllDocuments() {
        return new ArrayList<>(documentRepository.findAll());
    }

    /**
     * Возвращает список всех документов по заданному статусу.
     * @param status - статус процесса
     * @return список подходящих документов
     */
    @Override
    public Set<Document> findDocumentsByProcessStatus(DocProcessStatus status) {
        return documentRepository.findDocumentsByProcessStatus(status);
    }

    @Override
    public List<Document> findDocuments(Long userId,
                                        String text,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Long creatorId,
                                        Pageable pageable) {
        User user = userService.getUser(userId);
        BooleanBuilder booleanBuilder = new BooleanBuilder(QDocument.document.idOrganization.eq(user.getOrganization().getId()));
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("Даты поиска событий не верны");
        }
        if (text != null && !text.isBlank()) {
            booleanBuilder.and(QDocument.document.title.likeIgnoreCase("%" + text + "%"));
        }
        if (creatorId != null) {
            booleanBuilder.and(QDocument.document.ownerId.eq(creatorId));
        }
        if (rangeStart != null && rangeEnd != null) {
            booleanBuilder.and(QDocument.document.date.between(rangeStart, rangeEnd));
        } else {
            booleanBuilder.and(QDocument.document.date.before(LocalDateTime.now()));
        }
        List<Document> documents = new ArrayList<>();
        documents.addAll(documentRepository.findAll(booleanBuilder, pageable).getContent());
        return documents;
    }

    @Override
    @Transactional
    public void deleteDocumentById(Long id, Long userId) {
        if (Objects.equals(findDocumentById(id).getOwnerId(), userId)) {
            documentRepository.deleteById(id);
        } else {
            throw new BadRequestException("Можно удалять только свои документы");
        }
    }

    @Override
    public List<DocChanges> findDocChangesByDocumentId(Long id) {
        return docChangesRepository.findAllByDocumentId(id);
    }

    @Override
    public DocChanges findDocChangesById(Long id) {
        return docChangesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден лист изменений с id " + id));
    }

    @Override
    public List<DocChanges> findDocChangesByUserId(Long userId) {
        userService.getUser(userId);
        return docChangesRepository.findAllByUserChangerId(userId);
    }
}