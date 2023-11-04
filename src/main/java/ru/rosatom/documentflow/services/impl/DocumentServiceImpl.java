package ru.rosatom.documentflow.services.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.dto.DocAttributeValueCreateDto;
import ru.rosatom.documentflow.dto.DocParams;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocAttributeValuesRepository;
import ru.rosatom.documentflow.repositories.DocChangesRepository;
import ru.rosatom.documentflow.repositories.DocumentRepository;
import ru.rosatom.documentflow.services.*;

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
    final DocTypeService docTypeService;
    final DocAttributeValuesRepository docAttributeValuesRepository;
    final DocAttributeService docAttributeService;

    @Override
    @Transactional
    public Document createDocument(Document document, Long userId) {
        userOrganizationService.getOrganization(document.getIdOrganization());
        document.setOwnerId(userId);
        docAttributeValuesRepository.saveAll(document.getAttributeValues());
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
        StringBuilder sb = new StringBuilder();
        if (documentUpdateDto.getTitle() != null) {
            sb.append(String.format(CHANGE_TITLE, userId, document.getTitle(), documentUpdateDto.getTitle()));
            document.setTitle(documentUpdateDto.getTitle());
        }
        if (documentUpdateDto.getDate() != null) {
            sb.append(String.format(CHANGE_DATE, userId, document.getDate(), documentUpdateDto.getDate()));
            document.setDate(documentUpdateDto.getDate());
        }
        if (documentUpdateDto.getDocumentPath() != null) {
            sb.append(String.format(CHANGE_DOCUMENT_PATH, userId, document.getDocumentPath(), documentUpdateDto.getDocumentPath()));
            document.setDocumentPath(documentUpdateDto.getDocumentPath());
        }
        if (documentUpdateDto.getDocTypeId() != null) {
            sb.append(String.format(CHANGE_DOCUMENT_TYPE, userId, document.getDocType().getId(), documentUpdateDto.getDocTypeId()));
            document.setDocType(docTypeService.getDocTypeById(documentUpdateDto.getDocTypeId()));
        }
        if (documentUpdateDto.getAttributeValues().size() != 0) {
            List<DocAttributeValues> attributeValues = new ArrayList<>();
            for (DocAttributeValueCreateDto value : documentUpdateDto.getAttributeValues()) {
                DocAttributeValues values = new DocAttributeValues();
                values.setValue(value.getValue());
                values.setAttribute(docAttributeService.getDocAttributeById(value.getAttributeId()));
                attributeValues.add(values);
            }
            sb.append(String.format(CHANGE_DOCUMENT_ATTRIBUTES,
                    userId, document.getAttributeValues().toString(), documentUpdateDto.getAttributeValues().toString()));
            document.setAttributeValues(attributeValues);
        }
        docChanges.setChanges(sb.deleteCharAt(sb.length() - 1).toString());
        docAttributeValuesRepository.saveAll(document.getAttributeValues());
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
     *
     * @param status - статус процесса
     * @return список подходящих документов
     */
    @Override
    public Set<Document> findDocumentsByProcessStatus(DocProcessStatus status) {
        return documentRepository.findDocumentsByProcessStatus(status);
    }

    @Override
    public List<Document> findDocuments(Long userId,
                                        DocParams p,
                                        Pageable pageable) {
        User user = userService.getUser(userId);
        BooleanBuilder booleanBuilder = new BooleanBuilder(QDocument.document.idOrganization.eq(user.getOrganization().getId()));
        if (p.getRangeStart() != null && p.getRangeEnd() != null && p.getRangeEnd().isBefore(p.getRangeStart())) {
            throw new BadRequestException("Даты поиска событий не верны");
        }
        if (p.getText() != null && !p.getText().isBlank()) {
            booleanBuilder.and(QDocument.document.title.likeIgnoreCase("%" + p.getText() + "%"));
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
    public List<DocChanges> findDocChangesByDocumentId(Long id, Long userId) {
        return docChangesRepository.findAllByDocumentId(id);
    }

    @Override
    public DocChanges findDocChangesById(Long id, Long userId) {
        return docChangesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден лист изменений с id " + id));
    }

    @Override
    public List<DocChanges> findDocChangesByUserId(Long userId) {
        return docChangesRepository.findAllByUserChangerId(userId);
    }

    @Override
    public void updateFinalStatus(Document document, DocProcessStatus status) {
        document.setFinalDocStatus(status);
        documentRepository.save(document);
    }
}