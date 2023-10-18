package ru.rosatom.documentflow.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.repositories.DocumentRepository;
import ru.rosatom.documentflow.services.DocumentService;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    final DocumentRepository documentRepository;
    @Override
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document updateDocument(DocumentUpdateDto documentUpdateDto, Long id, Long userId) {
        // изменять можно только свои документы?
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден документ с id " + id));
        if (documentUpdateDto.getTitle() != null) {
            document.setTitle(document.getTitle());
        }
        if (documentUpdateDto.getDocumentPath() != null) {
            document.setDocumentPath(document.getDocumentPath());
        }
        // отслеживание изменений ?
        if (documentUpdateDto.getDocType() != null) {
            document.setDocType(document.getDocType());
        }
        if (documentUpdateDto.getOwner() != null) {
            document.setOwner(document.getOwner());
        }
        return documentRepository.save(document);
    }
    @Override
    public Document findDocumentById (Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Не найден документ с id " + id));
    }

    @Override
    public void deleteDocumentById (Long id) {
        documentRepository.deleteById(id);
    }

}