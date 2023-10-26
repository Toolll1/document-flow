package ru.rosatom.documentflow.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.adapters.CustomPageRequest;
import ru.rosatom.documentflow.dto.DocumentChangesDto;
import ru.rosatom.documentflow.dto.DocumentDto;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.mappers.DocumentChangesMapper;
import ru.rosatom.documentflow.mappers.DocumentMapper;
import ru.rosatom.documentflow.services.DocumentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.rosatom.documentflow.adapters.CommonUtils.*;


@Slf4j
@Validated
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/document/{userId}")
@RequiredArgsConstructor
public class DocumentController {

    final DocumentService documentService;
    final DocumentMapper dm;
    final DocumentChangesMapper cm;

    //создание нового документа
    @PostMapping
    public DocumentDto createDocument(@PathVariable Long userId, @RequestBody @Valid DocumentDto documentDto) {
        log.trace("Создание документа пользователем {} : {}", userId, documentDto);
        return dm.documentToDto(documentService.createDocument(dm.documentFromDto(documentDto), userId));
    }

    //поиск документа по id
    @GetMapping("/{documentId}")
    public DocumentDto getDocumentById(@PathVariable Long userId, @PathVariable Long documentId) {
        log.trace("Запрос информации о документе {} от пользователя {}", documentId, userId);
        return dm.documentToDto(documentService.findDocumentById(documentId));
    }

    //поиск документов по своей организации
    @GetMapping
    public List<DocumentDto> findDocuments(@PathVariable Long userId,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(required = false) Long creatorId,
                                           @RequestParam(defaultValue = PAGINATION_DEFAULT_FROM) @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = PAGINATION_DEFAULT_SIZE) @Positive Integer size) {
        log.trace("Запрос информации о документах своей организации от пользователя {}", userId);
        return documentService.findDocuments(userId, text, rangeStart,
                        rangeEnd, creatorId, new CustomPageRequest(from, size))
                .stream()
                .map(dm::documentToDto)
                .collect(Collectors.toList());
    }

    //поиск истории изменений по id документа
    @GetMapping("/{documentId}/changes")
    public List<DocumentChangesDto> findDocChangesByDocumentId(@PathVariable Long userId, @PathVariable Long documentId) {
        log.trace("Запрос информации о истории изменений документа {} от пользователя {}", documentId, userId);
        return documentService.findDocChangesByDocumentId(documentId)
                .stream()
                .map(cm::changesToDto)
                .collect(Collectors.toList());
    }

    // обновление документа
    @PatchMapping("/{documentId}")
    public DocumentDto updateDocument(@PathVariable Long userId, @PathVariable Long documentId,
                                      @RequestBody @Valid DocumentUpdateDto documentUpdateDto) {
        log.trace("Обновление информации о событии {} пользователем {}", documentId, userId);
        return dm.documentToDto(documentService.updateDocument(documentUpdateDto, documentId, userId));
    }

    //удаление документа
    @DeleteMapping("/{documentId}")
    public void deleteDocument(@PathVariable Long userId, @PathVariable Long documentId) {
        log.trace("Удаление документа {} пользователем {}", documentId, userId);
        documentService.deleteDocumentById(documentId, userId);
    }

    //поиск изменения по id
    @GetMapping("/changesById/{documentChangesId}")
    public DocumentChangesDto findDocChangesById(@PathVariable Long userId, @PathVariable Long documentChangesId) {
        log.trace("Запрос информации о изменений {} от пользователя {}", documentChangesId, userId);
        return cm.changesToDto(documentService.findDocChangesById(documentChangesId));
    }

    //поиск документов измененных пользователем
    @GetMapping("/changesByCreator/{creatorId}")
    public List<DocumentChangesDto> findDocChangesByUserId(@PathVariable Long userId, @PathVariable Long creatorId) {
        log.trace("Запрос информации о документах измененных пользователем {} от пользователя {}", creatorId, userId);
        return documentService.findDocChangesByUserId(creatorId)
                .stream()
                .map(cm::changesToDto)
                .collect(Collectors.toList());
    }
}