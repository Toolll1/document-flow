package ru.rosatom.documentflow.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.rosatom.documentflow.exceptions.IllegalProcessStatusException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocProcessRepository;
import ru.rosatom.documentflow.services.DocumentProcessService;
import ru.rosatom.documentflow.services.DocumentService;
import ru.rosatom.documentflow.services.EmailService;
import ru.rosatom.documentflow.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class DocumentProcessServiceImplTest {
    private final Random random = new Random();

    private final DocumentService documentService = Mockito.mock(DocumentService.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final DocProcessRepository docProcessRepository = Mockito.mock(DocProcessRepository.class);
    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final DocumentProcessService documentProcessService = new DocumentProcessServiceImpl(
            documentService,
            userService,
            docProcessRepository,
            emailService);


    @Nested
    class ApprovalTests {
        private DocProcess updatableDocProcess;
        private final ProcessUpdateRequest processUpdateRequest = new ProcessUpdateRequest();
        private final Document updatableDocProcessDocument = Mockito.mock(Document.class);

        @Test
        void testSendToApproveWithOneDocProcess() {
            updatableDocProcess = new DocProcess(1L,
                    updatableDocProcessDocument,
                    Mockito.mock(User.class),
                    Mockito.mock(User.class),
                    DocProcessStatus.NEW,
                    "");
            processUpdateRequest.setProcessId(updatableDocProcess.getId());

            Mockito.doReturn(Optional.of(updatableDocProcess))
                    .when(docProcessRepository).findById(updatableDocProcess.getId());

            documentProcessService.sendToApprove(processUpdateRequest);
            Mockito.verify(docProcessRepository).save(updatableDocProcess);

            Assertions.assertEquals(DocProcessStatus.WAITING_FOR_APPROVE, updatableDocProcess.getStatus());
        }

        @ParameterizedTest
        @EnumSource(value = DocProcessStatus.class, names = {"APPROVED", "REJECTED"})
        void testSendToApproveWithDocProcessesWhoseStatusShouldNotChange(DocProcessStatus docProcessStatus) {
            DocProcess docProcess = generateMockDocProcessWithStatus(docProcessStatus, updatableDocProcessDocument);
            processUpdateRequest.setProcessId(docProcess.getId());

            Mockito.doReturn(Optional.of(docProcess))
                    .when(docProcessRepository).findById(docProcess.getId());

            Assertions.assertThrows(IllegalProcessStatusException.class, () ->
                    documentProcessService.sendToApprove(processUpdateRequest));
        }

        @ParameterizedTest
        @EnumSource(value = DocProcessStatus.class, names = {"NEW", "CORRECTING"})
        void testSendToApproveWithDocProcessesWhoseStatusShouldChange(DocProcessStatus docProcessStatus) {
            DocProcess docProcess = generateMockDocProcessWithStatus(docProcessStatus, updatableDocProcessDocument);
            processUpdateRequest.setProcessId(docProcess.getId());

            Mockito.doReturn(Optional.of(docProcess))
                    .when(docProcessRepository).findById(docProcess.getId());

            documentProcessService.sendToApprove(processUpdateRequest);
            Mockito.verify(docProcessRepository).save(docProcess);

            Assertions.assertEquals(DocProcessStatus.WAITING_FOR_APPROVE, docProcess.getStatus());
        }

    }

    @Nested
    class CorrectionTests {
        private final ProcessUpdateRequest processUpdateRequest = new ProcessUpdateRequest();
        private DocProcess updatableDocProcess;
        private final Document
                updatableDocProcessDocument = Mockito.mock(Document.class);

        @BeforeEach
        void init() {
            updatableDocProcess = new DocProcess(1L,
                    updatableDocProcessDocument,
                    Mockito.mock(User.class),
                    Mockito.mock(User.class),
                    DocProcessStatus.WAITING_FOR_APPROVE,
                    "");
            processUpdateRequest.setProcessId(updatableDocProcess.getId());
            Mockito.doReturn(Optional.of(updatableDocProcess))
                    .when(docProcessRepository).findById(updatableDocProcess.getId());
            Mockito.when(docProcessRepository.findAllByDocumentId(updatableDocProcessDocument.getId()))
                    .thenReturn(List.of(updatableDocProcess));
        }

        @Test
        void testSendToCorrectionWithOneDocProcess() {
            documentProcessService.sendToCorrection(processUpdateRequest);
            Mockito.verify(docProcessRepository).save(updatableDocProcess);
            Assertions.assertEquals(DocProcessStatus.CORRECTING, updatableDocProcess.getStatus());
        }

        @Test
        void testSendToCorrectionWithMultipleDocProcesses() {
            List<DocProcess> docProcessesWhoseStatusShouldNotChange = List.of(
                    generateMockDocProcessWithStatus(DocProcessStatus.NEW, updatableDocProcessDocument)
            );
            List<DocProcess> docProcessesWhoseStatusShouldChange = List.of(
                    updatableDocProcess,
                    generateMockDocProcessWithStatus(DocProcessStatus.APPROVED, updatableDocProcessDocument),
                    generateMockDocProcessWithStatus(DocProcessStatus.REJECTED, updatableDocProcessDocument),
                    generateMockDocProcessWithStatus(DocProcessStatus.WAITING_FOR_APPROVE, updatableDocProcessDocument)
            );
            List<DocProcess> docProcesses = Stream.of(
                    docProcessesWhoseStatusShouldNotChange,
                    docProcessesWhoseStatusShouldChange
            ).flatMap(List::stream).collect(Collectors.toList());
            Mockito.when(docProcessRepository.findAllByDocumentId(updatableDocProcessDocument.getId()))
                    .thenReturn(docProcesses);
            documentProcessService.sendToCorrection(processUpdateRequest);
            Mockito.verify(docProcessRepository).saveAll(ArgumentMatchers.anyIterable());
            docProcessesWhoseStatusShouldChange.forEach(docProcess -> Assertions.assertEquals(DocProcessStatus.CORRECTING, docProcess.getStatus()));
            docProcessesWhoseStatusShouldNotChange.forEach(docProcess -> Assertions.assertNotEquals(DocProcessStatus.CORRECTING, docProcess.getStatus()));
        }

    }


    private DocProcess generateMockDocProcessWithStatus(DocProcessStatus status, Document document) {
        return new DocProcess(random.nextLong(),
                document,
                Mockito.mock(User.class),
                Mockito.mock(User.class),
                status,
                "");
    }
}