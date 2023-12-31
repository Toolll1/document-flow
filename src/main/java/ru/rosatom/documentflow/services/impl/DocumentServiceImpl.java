package ru.rosatom.documentflow.services.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.dto.DocAttributeValueCreateDto;
import ru.rosatom.documentflow.dto.DocParams;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.QDocAttributeValues;
import ru.rosatom.documentflow.models.QDocument;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.repositories.DocAttributeValuesRepository;
import ru.rosatom.documentflow.repositories.DocChangesRepository;
import ru.rosatom.documentflow.repositories.DocumentRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.DocTypeService;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.FileService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.rosatom.documentflow.adapters.CommonUtils.CHANGE_DOCUMENT_ATTRIBUTES;
import static ru.rosatom.documentflow.adapters.CommonUtils.CHANGE_DOCUMENT_TYPE;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    final DocumentRepository documentRepository;
    final DocChangesRepository docChangesRepository;
    final UserService userService;
    final UserOrganizationService userOrganizationService;
    final DocTypeService docTypeService;
    final DocAttributeValuesRepository docAttributeValuesRepository;
    final DocAttributeService docAttributeService;
    final FileService fileService;

    @Override
    @Transactional
    public Document createDocument(Document document, User user) {

        userOrganizationService.getOrganization(user.getOrganization().getId());
        document.setOwnerId(user.getId());
        document.setIdOrganization(user.getOrganization().getId());
        document.setDate(LocalDateTime.now());
        document.setFinalDocStatus(DocProcessStatus.NEW);
        docAttributeValuesRepository.saveAll(document.getAttributeValues());

        Document newDocument = fileService.createFile(document, null);

        return documentRepository.save(newDocument);
    }

    @Override
    @Transactional
    public Document updateDocument(DocumentUpdateDto documentUpdateDto, Long id, Long userId) {
        Document newDocument = findDocumentById(id);

        checkFinalStatus(newDocument, "Запрещено изменять документы, находящиеся в конечном статусе");

        Document oldDocument = findDocumentById(id);
        DocChanges docChanges = new DocChanges();
        StringBuilder sb = new StringBuilder();

        docChanges.setDocumentId(id);
        docChanges.setDateChange(LocalDate.now());
        docChanges.setPreviousVersion(newDocument.getDocumentPath());
        docChanges.setUserChangerId(userId);

        if (documentUpdateDto.getDocTypeId() != null) {
            sb.append(String.format(CHANGE_DOCUMENT_TYPE, userId, newDocument.getDocType().getId(), documentUpdateDto.getDocTypeId()));
            newDocument.setDocType(docTypeService.getDocTypeById(documentUpdateDto.getDocTypeId()));
        }

        if (documentUpdateDto.getAttributeValues() != null) {
            if (!documentUpdateDto.getAttributeValues().isEmpty()) {
                List<DocAttributeValues> attributeValues = new ArrayList<>();
                for (DocAttributeValueCreateDto value : documentUpdateDto.getAttributeValues()) {
                    DocAttributeValues values = new DocAttributeValues();
                    values.setValue(value.getValue());
                    values.setAttribute(docAttributeService.getDocAttributeById(value.getAttributeId()));
                    attributeValues.add(values);
                }
                sb.append(String.format(CHANGE_DOCUMENT_ATTRIBUTES,
                        userId, newDocument.getAttributeValues().toString(), documentUpdateDto.getAttributeValues().toString()));
                newDocument.setAttributeValues(attributeValues);
            }
        }

        if (!sb.toString().isBlank() && !sb.toString().isEmpty()) {
            docChanges.setChanges(sb.deleteCharAt(sb.length() - 1).toString());
            docAttributeValuesRepository.saveAll(newDocument.getAttributeValues());
            docChangesRepository.save(docChanges);
        }
        newDocument.setTitle(documentUpdateDto.getTitle());

        Document correctDocument = fileService.updateFile(newDocument, oldDocument, null);

        return documentRepository.save(correctDocument);
    }

    @Override
    public Document findDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден документ с id " + documentId));
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Возвращает список всех документов по заданному статусу.
     *
     * @param status - статус процесса
     * @return список подходящих документов
     */
    @Override
    public Set<Document> findDocumentsByProcessStatus(DocProcessStatus status) {
        return documentRepository.findDocumentsByProcessStatus(status);
    }

    @Override
    public Set<Document> findDocumentsByProcessStatusAndIdOrganization(DocProcessStatus status, Long id) {
        return documentRepository.findDocumentsByProcessStatusAndIdOrganization(status, id);
    }

    @Override
    public Page<Document> findDocuments(Long userId, DocParams p, Pageable pageable) {
        User user = userService.getUser(userId);
        BooleanBuilder booleanBuilder = new BooleanBuilder(QDocument.document.idOrganization.eq(user.getOrganization().getId()));
        if (p.getRangeStart() != null && p.getRangeEnd() != null && p.getRangeEnd().isBefore(p.getRangeStart())) {
            throw new BadRequestException("Даты поиска событий не верны");
        }
        if (p.getText() != null && !p.getText().isBlank()) {
            booleanBuilder.and(QDocument.document.name.likeIgnoreCase("%" + p.getText() + "%"));
        }
        if (p.getCreatorId() != null) {
            booleanBuilder.and(QDocument.document.ownerId.eq(p.getCreatorId()));
        }
        if (p.getRangeStart() != null && p.getRangeEnd() != null) {
            booleanBuilder.and(QDocument.document.date.between(p.getRangeStart(), p.getRangeEnd()));
        } else {
            booleanBuilder.and(QDocument.document.date.before(LocalDateTime.now()));
        }
        if (p.getTypeId() != null) {
            booleanBuilder.and(QDocument.document.docType.id.eq(p.getTypeId()));
        }
        if (p.getAttributeId() != null) {
            booleanBuilder.and(QDocument.document.attributeValues.any().attribute.id.eq(p.getAttributeId()));
        }
        if (p.getAttributeValue() != null && !p.getAttributeValue().isBlank()) {
            QDocAttributeValues qAttributeValue = QDocAttributeValues.docAttributeValues;
            booleanBuilder.and(QDocument.document.attributeValues.any().in(
                    JPAExpressions.select(qAttributeValue)
                            .from(qAttributeValue)
                            .where(qAttributeValue.value.lower().likeIgnoreCase("%" + p.getAttributeValue().toLowerCase() + "%"))
            ));
        }
        return documentRepository.findAll(booleanBuilder, pageable);
    }

    @Override
    @Transactional
    public void deleteDocumentById(Long id, Long userId) {

        Document document = findDocumentById(id);

        checkFinalStatus(document, "Запрещено удалять документы, находящиеся в конечном статусе");

        if (Objects.equals(document.getOwnerId(), userId)) {
            fileService.deleteFile(document);
            documentRepository.deleteById(id);
        } else {
            throw new BadRequestException("Можно удалять только свои документы");
        }
    }

    private void checkFinalStatus(Document document, String text) {

        if (document.getFinalDocStatus() != null) {
            if (document.getFinalDocStatus().equals(DocProcessStatus.APPROVED) || document.getFinalDocStatus().equals(DocProcessStatus.REJECTED)) {
                throw new BadRequestException(text);
            }
        }
    }


    @Override
    public Page<DocChanges> findDocChangesByDocumentId(Long id, Pageable
            pageable, Optional<Long> orgId) {
        return orgId.map(oId -> docChangesRepository.findAllByDocumentIdAndOrgId(id, oId, pageable))
                .orElse(docChangesRepository.findAllByDocumentId(id, pageable));
    }

    @Override
    public DocChanges findDocChangesById(Long id) {
        return docChangesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден лист изменений с id " + id));
    }

    @Override
    public List<DocChanges> findDocChangesByUserId(Long userId) {
        return docChangesRepository.findAllByUserChangerId(userId);
    }

    @Override
    public void updateFinalStatus(Document newDocument, DocProcessStatus
            status, Collection<DocProcess> docProcess) {
        newDocument.setFinalDocStatus(status);

        if (status.equals(DocProcessStatus.APPROVED)) {
            Document oldDocument = findDocumentById(newDocument.getId());
            Document correctDocument = fileService.updateFile(newDocument, oldDocument, docProcess);

            documentRepository.save(correctDocument);
        } else {
            documentRepository.save(newDocument);
        }
    }
}