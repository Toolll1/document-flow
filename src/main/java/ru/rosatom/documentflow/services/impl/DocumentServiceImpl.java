package ru.rosatom.documentflow.services.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.adapters.TranslitText;
import ru.rosatom.documentflow.dto.*;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.repositories.DocAttributeValuesRepository;
import ru.rosatom.documentflow.repositories.DocChangesRepository;
import ru.rosatom.documentflow.repositories.DocumentRepository;
import ru.rosatom.documentflow.services.*;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    final UserMapper userMapper;
    static final String pathToFiles = "src\\main\\java\\ru\\rosatom\\documentflow\\files\\";
    private final MinioClient minioClient = MinioClient.builder()
            .endpoint("http://minio:9000")
            //.endpoint("http://localhost:9000") // для запуска нашего сервиса локально
            .credentials("admin", "Secure123$")
            .build();

    @Override
    @Transactional
    public Document createDocument(Document document, Long userId) {
        String name = document.getDocType().getName() + userId + ".docx"; // удалить при включении минио

        userOrganizationService.getOrganization(document.getIdOrganization());
        document.setOwnerId(userId);
        document.setDate(LocalDateTime.now());
        docAttributeValuesRepository.saveAll(document.getAttributeValues());
        document.setName(name); // удалить при включении минио
        document.setDocumentPath(new File(pathToFiles + name).getAbsolutePath()); // удалить при включении минио

        Document newDocument = documentRepository.save(document);

        createLocalFile(newDocument);

        return findDocumentById(newDocument.getId());
    }

    private void createLocalFile(Document document) {

        User user = userService.getUser(document.getOwnerId());
        String name = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + document.getId() + ".docx";
        String path = new File(pathToFiles + name).getAbsolutePath();
        File file = new File(path);

        if (file.exists()) {
            throw new ConflictException("Такой файл уже существует");
        }

        try {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            PPr paragraphProperties = new PPr();
            Jc justification = new Jc();
            UserReplyDto userReplyDto = userMapper.objectToReplyDto(user);

            justification.setVal(JcEnumeration.RIGHT);
            paragraphProperties.setJc(justification);
            mainDocumentPart.addStyledParagraphOfText("Title", document.getDocType().getName());
            mainDocumentPart.addParagraphOfText("ФИО: " + userReplyDto.getFullName()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("Дата рождения: " + userReplyDto.getDateOfBirth()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("Организация: " +userReplyDto.getOrgDto().getName()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("ИНН: " + userReplyDto.getOrgDto().getInn()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("\n\n");
            mainDocumentPart.addParagraphOfText("Значения атрибутов:");

            for (DocAttributeValues attributeValue : document.getAttributeValues()) {
                mainDocumentPart.addParagraphOfText(attributeValue.getAttribute().getName() + ": " + attributeValue.getValue());
            }

            File exportFile = new File(path);
            wordPackage.save(exportFile);

        } catch (Docx4JException e) {
            throw new BadRequestException(e.getMessage());
        }

        //  addFileToMinio(document, title, file); //для сохранения файла в минио (запуск из контейнера)
    }

    private void deleteLocalFile(File file) {

        if (!file.delete()) {
            throw new BadRequestException("Ошибка при удалении файла");
        }
    }

    private void addFileToMinio(Document document, String name, File file) {

        String bucketName = TranslitText.transliterate(document.getDocType().getName().replaceAll(" ", "").toLowerCase());

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .filename(file.getAbsolutePath())
                    .build());

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new BadRequestException(e.getMessage());
        } finally {
            deleteLocalFile(file);
        }

        document.setDocumentPath("http://127.0.0.1:9090/browser/" + bucketName);
        document.setName(name);

        documentRepository.save(document);
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
        if (documentUpdateDto.getName() != null) {
            sb.append(String.format(CHANGE_TITLE, userId, document.getName(), documentUpdateDto.getName()));
            document.setName(documentUpdateDto.getName());
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

        if (documentUpdateDto.getAttributeValues() != null) {
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
        return new ArrayList<>(documentRepository.findAll(booleanBuilder, pageable).getContent());
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