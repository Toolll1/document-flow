package ru.rosatom.documentflow.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.services.DocumentService;

import javax.validation.Valid;


@Slf4j
@Validated
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/document/{userId}")
@RequiredArgsConstructor
public class DocumentController {

    final DocumentService documentService;

    @PostMapping
    public Document createDocument(@PathVariable Long userId, @RequestBody @Valid Document document) {
        log.trace("Создание документа пользователем {} : {}", userId, document);
        return documentService.createDocument(document);
    }

    @GetMapping("/{documentId}")
    public Document getDocumentById(@PathVariable Long userId, @PathVariable Long documentId) {
        log.trace("Запрос информации о документе {} от пользователя {}", documentId, userId);
        return documentService.findDocumentById(documentId);
    }

    @PatchMapping("/{documentId}")
    public Document patchEvent(@PathVariable Long userId, @PathVariable Long documentId,
                                   @RequestBody @Valid DocumentUpdateDto documentUpdateDto) {
        log.trace("Обновление информации о событии {} пользователем {}", documentId, userId);
        return documentService.updateDocument(documentUpdateDto, documentId, userId);
    }

    @DeleteMapping("/{documentId}")
    public void deleteDocument(@PathVariable Long userId, @PathVariable long documentId) {
        log.trace("Удаление документа {} пользователем {}", documentId, userId);
        documentService.deleteDocumentById(documentId);
    }
}
