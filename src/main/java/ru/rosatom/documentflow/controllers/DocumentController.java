package ru.rosatom.documentflow.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.adapters.CustomPageRequest;
import ru.rosatom.documentflow.dto.DocumentChangesDto;
import ru.rosatom.documentflow.dto.DocumentDto;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.mappers.DocumentChangesMapper;
import ru.rosatom.documentflow.mappers.DocumentMapper;
import ru.rosatom.documentflow.models.User;
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
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    final DocumentService documentService;
    final DocumentMapper dm;
    final DocumentChangesMapper cm;

    //создание нового документа
    @PreAuthorize("#documentDto.ownerId==#user.id && hasAuthority('USER')")
    @PostMapping
    public DocumentDto createDocument(@RequestBody @Valid DocumentDto documentDto, @AuthenticationPrincipal User user) {
        log.trace("Создание документа пользователем {} : {}", user.getId(), documentDto);
        return dm.documentToDto(documentService.createDocument(dm.documentFromDto(documentDto), user.getId()));
    }

    //поиск документа по id
    @GetMapping("/{documentId}")
    public DocumentDto getDocumentById(@PathVariable Long documentId, @AuthenticationPrincipal User user) {
        log.trace("Запрос информации о документе {} от пользователя {}", documentId, user.getId());
        return dm.documentToDto(documentService.findDocumentById(documentId));
    }

    //поиск документов по своей организации
    @GetMapping
    public List<DocumentDto> findDocuments(@RequestParam(required = false) String text,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(required = false) Long creatorId,
                                           @RequestParam(defaultValue = PAGINATION_DEFAULT_FROM) @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = PAGINATION_DEFAULT_SIZE) @Positive Integer size,
                                           @AuthenticationPrincipal User user) {
        log.trace("Запрос информации о документах своей организации от пользователя {}", user.getId());
        return documentService.findDocuments(user.getId(), text, rangeStart,
                        rangeEnd, creatorId, new CustomPageRequest(from, size))
                .stream()
                .map(dm::documentToDto)
                .collect(Collectors.toList());
    }

    //поиск истории изменений по id документа
    @GetMapping("/{documentId}/changes")
    public List<DocumentChangesDto> findDocChangesByDocumentId(@PathVariable Long documentId, @AuthenticationPrincipal User user) {
        log.trace("Запрос информации о истории изменений документа {} от пользователя {}", documentId, user.getId());
        return documentService.findDocChangesByDocumentId(documentId)
                .stream()
                .map(cm::changesToDto)
                .collect(Collectors.toList());
    }

    // обновление документа
    @PreAuthorize("@documentProcessSecurityService.isCanManageProcess(#documentId,#user.id) && hasAuthority('USER')")
    @PatchMapping("/{documentId}")
    public DocumentDto updateDocument(@PathVariable Long documentId,
                                      @RequestBody @Valid DocumentUpdateDto documentUpdateDto, @AuthenticationPrincipal User user) {
        log.trace("Обновление информации о событии {} пользователем {}", documentId, user.getId());
        return dm.documentToDto(documentService.updateDocument(documentUpdateDto, documentId, user.getId()));
    }

    //удаление документа
    @PreAuthorize("@documentProcessSecurityService.isCanManageProcess(#documentId,#user.id) && hasAuthority('USER')")
    @DeleteMapping("/{documentId}")
    public void deleteDocument(@PathVariable Long documentId, @AuthenticationPrincipal User user) {
        log.trace("Удаление документа {} пользователем {}", documentId, user.getId());
        documentService.deleteDocumentById(documentId, user.getId());
    }

    //поиск изменения по id
    @GetMapping("/changesById/{documentChangesId}")
    public DocumentChangesDto findDocChangesById(@PathVariable Long documentChangesId, @AuthenticationPrincipal User user) {
        log.trace("Запрос информации о изменений {} от пользователя {}", documentChangesId, user.getId());
        return cm.changesToDto(documentService.findDocChangesById(documentChangesId));
    }

    //поиск документов измененных пользователем
    @GetMapping("/changesByCreator/{creatorId}")
    public List<DocumentChangesDto> findDocChangesByUserId(@PathVariable Long creatorId, @AuthenticationPrincipal User user) {
        log.trace("Запрос информации о документах измененных пользователем {} от пользователя {}", creatorId, user.getId());
        return documentService.findDocChangesByUserId(creatorId)
                .stream()
                .map(cm::changesToDto)
                .collect(Collectors.toList());
    }
}