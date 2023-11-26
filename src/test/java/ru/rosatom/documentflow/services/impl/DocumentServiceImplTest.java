package ru.rosatom.documentflow.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.repositories.DocAttributeValuesRepository;
import ru.rosatom.documentflow.repositories.DocChangesRepository;
import ru.rosatom.documentflow.repositories.DocumentRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.FileService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocChangesRepository docChangesRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserOrganizationService userOrganizationService;

    @Mock
    private DocTypeServiceImpl docTypeService;

    @Mock
    private DocAttributeValuesRepository docAttributeValuesRepository;

    private DocumentServiceImpl documentService;

    @InjectMocks
    private UserMapper userMapper;

    @InjectMocks
    private DocumentUpdateDto documentUpdateDto;

    @Mock
    private DocAttributeService docAttributesService;
    @Mock
    FileService fileService;


    private Document document;
    private User user;


    @BeforeEach
    public void setup() {
        //documentRepository = Mockito.mock(DocumentRepository.class);
        //documentService = new DocumentServiceImpl(documentRepository);
        UserOrganization userOrganization = UserOrganization.builder()
                .id(1L)
                .inn("1234567")
                .name("ООО Зеленоглазое такси")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Николай")
                .lastName("Петров")
                .organization(userOrganization)
                .dateOfBirth(LocalDate.now())
                .post("менеджер")
                .build();

        java.time.LocalDateTime time = LocalDateTime.now();
        DocType docType = new DocType();
        document = Document.builder()
                .id(1L)
                .name("documentOne")
                .documentPath("pathOne")
                .date(time)
                .ownerId(user.getId())
                .docType(docType)
                .idOrganization(userOrganization.getId())
                .attributeValues(new ArrayList<>())
                .build();


        documentService = new DocumentServiceImpl(
                documentRepository,
                docChangesRepository,
                userService,
                userOrganizationService,
                docTypeService,
                docAttributeValuesRepository,
                docAttributesService,
                fileService
        );

    }

    @Test
    public void getAllDocumentsTest() {
        java.time.LocalDateTime time = LocalDateTime.now();
        Document document2 = Document.builder()
                .id(2L)
                .name("documentTwo")
                .documentPath("pathTwo")
                .date(time)
                .ownerId(2L)
                .build();

        given(documentRepository.findAll()).willReturn(List.of(document, document2));
        List<Document> documentList = documentService.getAllDocuments();
        assertThat(documentList).isNotNull();
        assertThat(documentList.size()).isEqualTo(2);
    }

    void deleteFileIfExist(Path path) {
        File file = new File(path.toUri());
        if (file.exists()) {
            file.delete();
        }
    }

 /*   @SneakyThrows
    @Test
    public void createDocumentTest() {

        String fileName = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + document.getId() + ".docx";
        Path path = Paths.get(DocumentServiceImpl.pathToFiles, fileName).toAbsolutePath();
        deleteFileIfExist(path);

        Mockito.when(userService.getUser(user.getId())).thenReturn(user);
        Mockito.when(documentRepository.findById(document.getId())).thenReturn(Optional.ofNullable(document));
        Mockito.when(documentRepository.save(document)).thenReturn(document);

        Document documentCreated = documentService.createDocument(document, user.getId());

        assertThat(documentCreated).isNotNull();
        assertThat(document.getName()).isEqualTo(documentCreated.getName());
        assertThat((new File(path.toUri())).exists()).isTrue();
    }*/

 /*   @ParameterizedTest
    @EnumSource(AgreementType.class)
    public void updateDocumentTest(AgreementType agreementType) {
        String nameNew = "documentOneNewVersion";
        LocalDateTime dateNew = LocalDateTime.now();
        String docPathNew = "documentOneNewVersion";
        DocType docTypeNew = DocType.builder()
                .id(2L)
                .agreementType(agreementType)
                .build();

        documentUpdateDto.setName(nameNew);
        documentUpdateDto.setDate(dateNew);
        documentUpdateDto.setDocumentPath(docPathNew);
        documentUpdateDto.setDocTypeId(docTypeNew.getId());

        Mockito.when(documentRepository.findById(document.getId())).thenReturn(Optional.ofNullable(document));
        Mockito.when(docTypeService.getDocTypeById(docTypeNew.getId())).thenReturn(docTypeNew);

        documentService.updateDocument(documentUpdateDto, document.getId(), document.getOwnerId());

        assertThat(document.getName()).isEqualTo(nameNew);
        assertThat(document.getDate()).isEqualTo(dateNew);
        assertThat(document.getDocumentPath()).isEqualTo(docPathNew);
        assertThat(document.getDocType().getId()).isEqualTo(docTypeNew.getId());
    }*/

    @Test
    public void findDocumentByIdWhenFoundTest() {
        Mockito.when(documentRepository.findById(document.getId())).thenReturn(Optional.ofNullable(document));
        assertThat(documentService.findDocumentById(document.getId())).isEqualTo(document);
    }

    @Test
    public void findDocumentByIdWhenNotFoundTest() {
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> documentService.findDocumentById(document.getId()));
        assertThat("Не найден документ с id " + document.getId()).isEqualTo(exception.getMessage());
    }

/*    @ParameterizedTest
    @EnumSource(DocProcessStatus.class)
    public void updateFinalStatusTest(DocProcessStatus docProcessStatus) {
        documentService.updateFinalStatus(document, docProcessStatus, null);

        assertThat(document.getFinalDocStatus()).isEqualTo(docProcessStatus);
    }*/

}
