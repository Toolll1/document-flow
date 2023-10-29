package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentService {
    Document createDocument(Document document, Long userId);

    Document updateDocument(DocumentUpdateDto documentUpdateDto, Long id, Long UserId);

    List<DocChanges> findDocChangesByDocumentId(Long id);

    Document findDocumentById(Long documentId);

    List<Document> getAllDocuments();

    List<Document> findDocuments(Long userId,
                                 String text,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Long creatorId,
                                 Pageable pageable);

    void deleteDocumentById(Long id, Long userId);

    DocChanges findDocChangesById(Long id);

    List<DocChanges> findDocChangesByUserId(Long userId);
}
