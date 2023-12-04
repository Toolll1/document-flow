package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.dto.DocParams;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.models.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DocumentService {
    Document createDocument(Document document, Long userId);

    Document updateDocument(DocumentUpdateDto documentUpdateDto, Long id, Long UserId);

    Page<DocChanges> findDocChangesByDocumentId(Long id, Pageable pageable, Optional<Long> orgId);

    Document findDocumentById(Long documentId);

    List<Document> getAllDocuments();

    Set<Document> findDocumentsByProcessStatus(DocProcessStatus status);

    Page<Document> findDocuments(DocParams p,
                                 Pageable pageable);

    void deleteDocumentById(Long id, Long userId);

    DocChanges findDocChangesById(Long id);

    List<DocChanges> findDocChangesByUserId(Long userId);

    void updateFinalStatus(Document document, DocProcessStatus status, Collection<DocProcess> docProcess);
}
