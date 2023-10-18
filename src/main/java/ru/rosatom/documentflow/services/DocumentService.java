package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.models.Document;

public interface DocumentService {
    Document createDocument(Document document);

    Document updateDocument (DocumentUpdateDto documentUpdateDto, Long id, Long UserId);

    Document findDocumentById(Long id);

    void deleteDocumentById(Long id);
}
