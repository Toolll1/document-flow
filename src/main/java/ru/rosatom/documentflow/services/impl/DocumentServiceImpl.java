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
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
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
        DocType docType = docTypeService.getDocTypeById(document.getDocType().getId());
        if (docTypeService.isArchivedDocType(docType.getId())) {
            throw new ConflictException("Невозможно создать документ архивированного типа");
        }
        document.setOwnerId(user.getId());
        document.setIdOrganization(user.getOrganization().getId());
        document.setDate(LocalDateTime.now());
        document.setFinalDocStatus(DocProcessStatus.NEW);
        docAttributeValuesRepository.saveAll(document.getAttributeValues());

        Document newDocument = fileService.createFile(document, null);

        return documentRepository.save(newDocument);
    }

    /**
     * Обновляет существующий документ на основе предоставленных данных.
     *
     * @param documentUpdateDto DTO с данными для обновления документа
     * @param id                идентификатор документа, который нужно обновить
     * @param userId            идентификатор пользователя, выполняющего обновление
     * @return обновленный объект Document
     * @throws BadRequestException если документ находится в конечном статусе
     */
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

    /**
     * Ищет документ по его идентификатору.
     *
     * @param documentId идентификатор документа для поиска
     * @return найденный документ
     * @throws ObjectNotFoundException если документ с указанным id не найден
     */
    @Override
    public Document findDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден документ с id " + documentId));
    }

    /**
     * Возвращает список всех документов.
     *
     * @return список всех документов
     */
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

    /**
     * Находит документы по статусу обработки и идентификатору организации.
     *
     * @param status статус процесса обработки документов
     * @param id     идентификатор организации
     * @return множество документов, соответствующих заданным критериям
     */
    @Override
    public Set<Document> findDocumentsByProcessStatusAndIdOrganization(DocProcessStatus status, Long id) {
        return documentRepository.findDocumentsByProcessStatusAndIdOrganization(status, id);
    }

    /**
     * Поиск документов на основе различных критериев, включая диапазон дат, текст, тип и другие атрибуты.
     *
     * @param userId   идентификатор пользователя, выполняющего поиск
     * @param p        параметры поиска документов
     * @param pageable параметры пагинации
     * @return страница с документами, соответствующими заданным критериям
     * @throws BadRequestException если указаны неверные даты для поиска
     */
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

    /**
     * Удаляет документ по его идентификатору, если пользователь является владельцем документа.
     *
     * @param id     идентификатор документа для удаления
     * @param userId идентификатор пользователя, пытающегося удалить документ
     * @throws BadRequestException если пользователь не владелец документа или документ находится в конечном статусе
     */
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

    /**
     * Проверяет, находится ли документ в конечном статусе обработки.
     *
     * @param document документ для проверки статуса
     * @param text     текст исключения, если документ находится в конечном статусе
     * @throws BadRequestException если документ находится в конечном статусе
     */
    private void checkFinalStatus(Document document, String text) {

        if (document.getFinalDocStatus() != null) {
            if (document.getFinalDocStatus().equals(DocProcessStatus.APPROVED) || document.getFinalDocStatus().equals(DocProcessStatus.REJECTED)) {
                throw new BadRequestException(text);
            }
        }
    }

    /**
     * Находит изменения документа по его идентификатору.
     *
     * @param id       идентификатор документа
     * @param pageable параметры пагинации
     * @param orgId    необязательный идентификатор организации
     * @return страница с изменениями документа
     */
    @Override
    public Page<DocChanges> findDocChangesByDocumentId(Long id, Pageable
            pageable, Optional<Long> orgId) {
        return orgId.map(oId -> docChangesRepository.findAllByDocumentIdAndOrgId(id, oId, pageable))
                .orElse(docChangesRepository.findAllByDocumentId(id, pageable));
    }

    /**
     * Находит изменения документа по идентификатору изменений.
     *
     * @param id идентификатор изменений документа
     * @return найденные изменения документа
     * @throws ObjectNotFoundException если изменения с указанным id не найдены
     */
    @Override
    public DocChanges findDocChangesById(Long id) {
        return docChangesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден лист изменений с id " + id));
    }

    /**
     * Возвращает список изменений документов, сделанных определенным пользователем.
     *
     * @param userId идентификатор пользователя, изменения которого необходимо найти
     * @return список изменений документов, сделанных пользователем
     */
    @Override
    public List<DocChanges> findDocChangesByUserId(Long userId) {
        return docChangesRepository.findAllByUserChangerId(userId);
    }

    /**
     * Обновляет конечный статус документа. Если статус изменен на 'APPROVED', также обновляет файл документа.
     *
     * @param newDocument документ, статус которого обновляется
     * @param status      новый статус процесса документа
     * @param docProcess  коллекция процессов, связанных с документом (может быть null)
     */
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

    /**
     * Проверяет, существуют ли документы определенного типа.
     *
     * @param docTypeId идентификатор типа документа
     * @return true, если существуют документы данного типа, иначе false
     */
    public boolean existsDocumentsByType(Long docTypeId) {
        return documentRepository.existsByDocTypeId(docTypeId);
    }
}