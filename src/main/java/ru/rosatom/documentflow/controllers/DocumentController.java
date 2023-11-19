package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.adapters.CustomPageRequest;
import ru.rosatom.documentflow.dto.*;
import ru.rosatom.documentflow.mappers.DocumentChangesMapper;
import ru.rosatom.documentflow.mappers.DocumentMapper;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.Document;
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
@Tag(name = "Документы")
public class DocumentController {

  final DocumentService documentService;
  final DocumentMapper dm;
  final DocumentChangesMapper cm;
  private final ModelMapper modelMapper;


  // создание нового документа
  @Operation(summary = "Добавить новый документ")
  @PreAuthorize("hasAuthority('USER')")
  @PostMapping
  @SecurityRequirement(name = "JWT")
  public DocumentDto createDocument(
      @RequestBody @Valid @Parameter(description = "DTO создания объекта") DocumentCreateDto documentDto,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace("Создание документа пользователем {} : {}", user.getId(), documentDto);
    Document documentFromDto = dm.documentFromCreateDto(documentDto);
    Document docCreate =  documentService.createDocument(documentFromDto, user.getId());
    return modelMapper.map(docCreate, DocumentDto.class) ;
  }

  // поиск документа по id
  @Operation(summary = "Получить документ по ID")
  @GetMapping("/{documentId}")
  @SecurityRequirement(name = "JWT")
  public DocumentDto getDocumentById(
      @PathVariable @Parameter(description = "ID документа") Long documentId,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace("Запрос информации о документе {} от пользователя {}", documentId, user.getId());
    Document document = documentService.findDocumentById(documentId);
    return modelMapper.map(document, DocumentDto.class);
  }

  // поиск документов по своей организации
  @Operation(summary = "Получить документы по своей организации")
  @GetMapping
  @SecurityRequirement(name = "JWT")
  public List<DocumentDto> findDocuments(
      @RequestParam(required = false) @Parameter(description = "Имя документа") String text,
      @RequestParam(required = false)
          @DateTimeFormat(pattern = DATE_TIME_PATTERN)
          @Parameter(description = "Начало интервала")
          LocalDateTime rangeStart,
      @RequestParam(required = false)
          @DateTimeFormat(pattern = DATE_TIME_PATTERN)
          @Parameter(description = "Конец интервала")
          LocalDateTime rangeEnd,
      @RequestParam(required = false) @Parameter(description = "ID создателя документа")
          Long creatorId,
      @RequestParam(defaultValue = PAGINATION_DEFAULT_FROM)
          @PositiveOrZero
          @Parameter(description = "Номер страницы")
          Integer from,
      @RequestParam(defaultValue = PAGINATION_DEFAULT_SIZE)
          @Positive
          @Parameter(description = "Количество элементов на странице")
          Integer size,
      @RequestParam(required = false) @Parameter(description = "ID типа документа") Long typeId,
      @RequestParam(required = false) @Parameter(description = "ID атрибута документа")
          Long attributeId,
      @RequestParam(required = false) @Parameter(description = "Значение атрибута")
          String attributeValue,
      @AuthenticationPrincipal @Parameter(name = "user", hidden = true) User user) {
    log.trace("Запрос информации о документах своей организации от пользователя {}", user.getId());
    return documentService
        .findDocuments(
            user.getId(),
            new DocParams(
                text, rangeStart, rangeEnd, creatorId, typeId, attributeId, attributeValue),
            new CustomPageRequest(from, size))
        .stream()
        .map(o -> modelMapper.map(o, DocumentDto.class ))
        .collect(Collectors.toList());
  }

  // поиск истории изменений по id документа
  @Operation(summary = "Получить историю изменений по ID")
  @GetMapping("/{documentId}/changes")
  @SecurityRequirement(name = "JWT")
  public List<DocumentChangesDto> findDocChangesByDocumentId(
      @PathVariable @Parameter(description = "ID документа") Long documentId,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace(
        "Запрос информации о истории изменений документа {} от пользователя {}",
        documentId,
        user.getId());
    return documentService.findDocChangesByDocumentId(documentId, user.getId()).stream()
        .map(o->modelMapper.map(o, DocumentChangesDto.class))
        .collect(Collectors.toList());
  }

  // обновление документа
  @Operation(summary = "Изменить документ")
  @PreAuthorize(
      "@documentProcessSecurityService.isCanManageProcess(#documentId,#user.id) && hasAuthority('USER')")
  @PatchMapping("/{documentId}")
  @SecurityRequirement(name = "JWT")
  public DocumentDto updateDocument(
      @PathVariable @Parameter(description = "ID документа") Long documentId,
      @RequestBody @Valid @Parameter(description = "DTO изменения документа") DocumentUpdateDto documentUpdateDto,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace("Обновление информации о событии {} пользователем {}", documentId, user.getId());
    Document updateDocument = documentService.updateDocument(documentUpdateDto, documentId, user.getId());
    return modelMapper.map(updateDocument, DocumentDto.class);
  }

  // удаление документа
  @Operation(summary = "Удалить документ")
  @PreAuthorize(
      "@documentProcessSecurityService.isCanManageProcess(#documentId,#user.id) && hasAuthority('USER')")
  @DeleteMapping("/{documentId}")
  @SecurityRequirement(name = "JWT")
  public void deleteDocument(
      @PathVariable @Parameter(description = "ID документа") Long documentId,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace("Удаление документа {} пользователем {}", documentId, user.getId());
    documentService.deleteDocumentById(documentId, user.getId());
  }

  // поиск изменения по id
  @Operation(summary = "Получить изменения документа по ID")
  @GetMapping("/changesById/{documentChangesId}")
  @SecurityRequirement(name = "JWT")
  public DocumentChangesDto findDocChangesById(
      @PathVariable @Parameter(description = "ID изменения документа") Long documentChangesId,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace(
        "Запрос информации о изменений {} от пользователя {}", documentChangesId, user.getId());
    DocChanges document = documentService.findDocChangesById(documentChangesId, user.getId());
    return modelMapper.map(document, DocumentChangesDto.class);
  }

  // поиск документов измененных пользователем
  @Operation(summary = "Получить документы измененные пользователем")
  @GetMapping("/changesByCreator/{creatorId}")
  @SecurityRequirement(name = "JWT")
  public List<DocumentChangesDto> findDocChangesByUserId(
      @PathVariable @Parameter(description = "ID создателя документа") Long creatorId,
      @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
    log.trace(
        "Запрос информации о документах измененных пользователем {} от пользователя {}",
        creatorId,
        user.getId());
    return documentService.findDocChangesByUserId(creatorId).stream()
        .map(o-> modelMapper.map(o, DocumentChangesDto.class))
        .collect(Collectors.toList());
  }
}
