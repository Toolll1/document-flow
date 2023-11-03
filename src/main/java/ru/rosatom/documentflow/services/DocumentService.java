package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.dto.DocParams;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;

import java.util.List;
import java.util.Set;

public interface DocumentService {
    Document createDocument(Document document, Long userId);

    Document updateDocument(DocumentUpdateDto documentUpdateDto, Long id, Long UserId);

    List<DocChanges> findDocChangesByDocumentId(Long id, Long userId);

    Document findDocumentById(Long documentId);

    List<Document> getAllDocuments();

    Set<Document> findDocumentsByProcessStatus(DocProcessStatus status);

    List<Document> findDocuments(Long userId,
                                 DocParams p,
                                 Pageable pageable);

    void deleteDocumentById(Long id, Long userId);

    DocChanges findDocChangesById(Long id, Long userId);

    List<DocChanges> findDocChangesByUserId(Long userId, Long id);
}
