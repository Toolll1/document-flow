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
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.DocParams;
import ru.rosatom.documentflow.dto.DocumentChangesDto;
import ru.rosatom.documentflow.dto.DocumentCreateDto;
import ru.rosatom.documentflow.dto.DocumentDto;
import ru.rosatom.documentflow.dto.DocumentUpdateDto;
import ru.rosatom.documentflow.mappers.DocumentChangesMapper;
import ru.rosatom.documentflow.mappers.DocumentMapper;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.DocumentService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.rosatom.documentflow.adapters.CommonUtils.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/v2/document")
@RequiredArgsConstructor
@Tag(name = "Документы")
public class DocumentController {

    final DocumentService documentService;
    final DocumentMapper dm;
    final DocumentChangesMapper cm;
    private final ModelMapper modelMapper;

    @Operation(summary = "Добавить новый документ")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public DocumentDto createDocument(
            @RequestBody @Valid @Parameter(description = "DTO создания документа") DocumentCreateDto documentDto,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        log.trace("Создание документа пользователем {} : {}", user.getId(), documentDto);
        Document documentFromDto = dm.documentFromCreateDto(documentDto);
        Document docCreate = documentService.createDocument(documentFromDto, user);
        return modelMapper.map(docCreate, DocumentDto.class);
    }

    @Operation(summary = "Получить документ по ID")
    @GetMapping("/{documentId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("@documentProcessSecurityService.isMyCompany(#documentId,#user.id) && " +
            "(hasAuthority('USER') || hasAuthority('COMPANY_ADMIN'))")
    public DocumentDto getDocumentById(
            @PathVariable @Parameter(description = "ID документа") Long documentId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        log.trace("Запрос информации о документе {} от пользователя {}", documentId, user.getId());
        Document document = documentService.findDocumentById(documentId);
        return modelMapper.map(document, DocumentDto.class);
    }

    @Operation(summary = "Получить документы по своей организации")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public Page<DocumentDto> findDocuments(
            @RequestParam(required = false) @Parameter(description = "Имя документа")
            String text,
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
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) @Parameter(description = "ID типа документа")
            Long typeId,
            @RequestParam(required = false) @Parameter(description = "ID атрибута документа")
            Long attributeId,
            @RequestParam(required = false) @Parameter(description = "Значение атрибута")
            String attributeValue,
            @AuthenticationPrincipal @Parameter(name = "user", hidden = true) User user) {
        log.trace("Запрос информации о документах своей организации от пользователя {}", user.getId());
        return documentService
                .findDocuments(user.getId(), new DocParams(text, rangeStart, rangeEnd, creatorId, typeId, attributeId, attributeValue), pageable)
                .map(o -> modelMapper.map(o, DocumentDto.class));
    }

    @Operation(summary = "Получить историю изменений по ID")
    @GetMapping("/{documentId}/changes")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize(
            "(@documentProcessSecurityService.isCanManageProcess(#documentId,#user.id) && hasAuthority('USER')) || " +
            "(@documentProcessSecurityService.isMyCompany(#documentId,#user.id) && hasAuthority('COMPANY_ADMIN'))")
    public Page<DocumentChangesDto> findDocChangesByDocumentId(
            @PathVariable @Parameter(description = "ID документа") Long documentId,
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user,
            @RequestParam(required = false, name = "org_id") @Parameter(description = "ID организации") Optional<Long> orgId
    ) {
        log.trace(
                "Запрос информации о истории изменений документа {} от пользователя {}",
                documentId,
                user.getId());
        return documentService.findDocChangesByDocumentId(documentId, pageable, orgId)
                .map(o -> modelMapper.map(o, DocumentChangesDto.class));
    }

    @Operation(summary = "Изменить документ")
    @PreAuthorize(
            "(@documentProcessSecurityService.isCanManageProcess(#documentId,#user.id) && hasAuthority('USER')) || " +
            "(@documentProcessSecurityService.isMyCompany(#documentId,#user.id) && hasAuthority('COMPANY_ADMIN'))")
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

    @Operation(summary = "Удалить документ")
    @PreAuthorize(
            "(@documentProcessSecurityService.isCanManageProcess(#documentId, #user.id) && hasAuthority('USER')) || " +
            "(@documentProcessSecurityService.isMyCompany(#documentId,#user.id) && hasAuthority('COMPANY_ADMIN'))")
    @DeleteMapping("/{documentId}")
    @SecurityRequirement(name = "JWT")
    public void deleteDocument(
            @PathVariable @Parameter(description = "ID документа") Long documentId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        log.trace("Удаление документа {} пользователем {}", documentId, user.getId());
        documentService.deleteDocumentById(documentId, user.getId());
    }

    @Operation(summary = "Получить изменения документа по ID")
    @GetMapping("/changesById/{documentChangesId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize(
            "@documentProcessSecurityService.isMyCompanyChanges(#documentChangesId, #user.id) && (hasAuthority('USER') || hasAuthority('COMPANY_ADMIN'))")
    public DocumentChangesDto findDocChangesById(
            @PathVariable @Parameter(description = "ID изменения документа") Long documentChangesId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        log.trace(
                "Запрос информации о изменений {} от пользователя {}", documentChangesId, user.getId());
        DocChanges document = documentService.findDocChangesById(documentChangesId);
        return modelMapper.map(document, DocumentChangesDto.class);
    }

    @Operation(summary = "Получить документы измененные пользователем")
    @GetMapping("/changesByCreator/{creatorId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("@documentProcessSecurityService.isSameCompany(#creatorId, #user.id) && (hasAuthority('USER') || hasAuthority('COMPANY_ADMIN'))")
    public List<DocumentChangesDto> findDocChangesByUserId(
            @PathVariable @Parameter(description = "ID создателя документа") Long creatorId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        log.trace(
                "Запрос информации о документах измененных пользователем {} от пользователя {}",
                creatorId,
                user.getId());
        return documentService.findDocChangesByUserId(creatorId).stream()
                .map(o -> modelMapper.map(o, DocumentChangesDto.class))
                .collect(Collectors.toList());
    }
}
