package ru.rosatom.documentflow.services.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rosatom.documentflow.adapters.TranslitText;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.mappers.DocumentMapper;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.*;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserOrganizationRepository userOrganizationRepository;

    @Mock
    private UserOrganizationService userOrganizationService;

    @Mock
    private DocTypeRepository docTypeRepository;

    @Mock
    private DocTypeServiceImpl docTypeService;

    @Mock
    private DocAttributeValuesRepository docAttributeValuesRepository;

    @InjectMocks
    private DocAttributeServiceImpl docAttributeService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @InjectMocks
    private UserMapper userMapper;

    @InjectMocks
    private DocumentMapper documentMapper;

    @InjectMocks
    private DocumentUpdateDto documentUpdateDto;


    private Document document;
    private UserOrganization userOrganization;
    private User user;
    private DocType docType;

    @SneakyThrows
    private void setField(Object obj, String fieldName, Object fieldValue) {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }

    @BeforeEach
    public void setup(){
        //documentRepository = Mockito.mock(DocumentRepository.class);
        //documentService = new DocumentServiceImpl(documentRepository);
        userOrganization = UserOrganization.builder()
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
        docType = new DocType();
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

        setField(documentService, "userMapper", userMapper);

    }

    @Test
    public void getAllDocumentsTest(){
        java.time.LocalDateTime time = LocalDateTime.now();
        Document document2 = Document.builder()
                .id(2L)
                .name("documentTwo")
                .documentPath("pathTwo")
                .date(time)
                .ownerId(2L)
                .build();

        given(documentRepository.findAll()).willReturn(List.of(document,document2));
        List<Document> documentList = documentService.getAllDocuments();
        assertThat(documentList).isNotNull();
        assertThat(documentList.size()).isEqualTo(2);
    }

    @SneakyThrows
    @Test
    public void createDocumentTest(){
        List<String> pathParts = List.of("src", "main", "java", "ru", "rosatom", "documentflow", "files");
        Path path = Path.of("");
        for (String pathPart : pathParts) {
            path = Paths.get(path.toString(), pathPart);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        }

        String fileName = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + document.getId() + ".docx";
        path = Paths.get(path.toString(), fileName).toAbsolutePath();
        File file = new File(path.toUri());
        if (file.exists()){
            file.delete();
        }

        Mockito.when(userService.getUser(user.getId())).thenReturn(user);
        Mockito.when(documentRepository.findById(document.getId())).thenReturn(Optional.ofNullable(document));
        Mockito.when(documentRepository.save(document)).thenReturn(document);

        Document documentCreated = documentService.createDocument(document, user.getId());

        assertThat(documentCreated).isNotNull();
        assertThat(document.getName()).isEqualTo(documentCreated.getName());
        assertThat((new File(path.toUri())).exists()).isTrue();
    }

    @ParameterizedTest
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
    }

    @Test
    public void findDocumentByIdWhenFoundTest(){
        Mockito.when(documentRepository.findById(document.getId())).thenReturn(Optional.ofNullable(document));
        assertThat(documentService.findDocumentById(document.getId())).isEqualTo(document);
    }

    @Test
    public void findDocumentByIdWhenNotFoundTest(){
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> documentService.findDocumentById(document.getId()));
        assertThat("Не найден документ с id " + document.getId()).isEqualTo(exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(DocProcessStatus.class)
    public void updateFinalStatusTest(DocProcessStatus docProcessStatus) {
        documentService.updateFinalStatus(document, docProcessStatus);

        assertThat(document.getFinalDocStatus()).isEqualTo(docProcessStatus);
    }

}
