package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocAttributeCreateDto;
import ru.rosatom.documentflow.dto.DocAttributeDto;
import ru.rosatom.documentflow.dto.DocAttributeUpdateRequestDto;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.DocAttributeService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/docattributes")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Атрибуты документа")
public class DocAttributeController {

    private final DocAttributeService docAttributeService;
    private final ModelMapper modelMapper;


    @Operation(
            summary = "Получить все атрибуты",
            description = "Все атрибуты с пагинацией и сортировкой")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    Page<DocAttributeDto> getAllDocTypes(@ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @AuthenticationPrincipal @Parameter(hidden = true) User user) {
        return docAttributeService
                .getAllDocAttributes(pageable,user)
                .map(o -> modelMapper.map(o, DocAttributeDto.class));
    }

    @Operation(summary = "Получить атрибут по ID")
    @GetMapping("/{docAttributeId}")
    @SecurityRequirement(name = "JWT")
    public DocAttributeDto getAttribute(
            @PathVariable @Parameter(description = "ID атрибута") Long docAttributeId) {
        DocAttribute docAttribute = docAttributeService.getDocAttributeById(docAttributeId);
        log.info("Получен запрос на получение DocAttribute с ID: {}", docAttributeId);
        return modelMapper.map(docAttribute, DocAttributeDto.class);
    }

    @Operation(summary = "Создать атрибут")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    public DocAttributeDto createAttribute(
            @Valid @RequestBody @Parameter(description = "DTO создания атрибута") DocAttributeCreateDto docAttributeCreateDto) {
        DocAttributeCreationRequest docAttributeCreationRequest =
                modelMapper.map(docAttributeCreateDto, DocAttributeCreationRequest.class);
        DocAttribute docAttribute = docAttributeService.createDocAttribute(docAttributeCreationRequest);
        log.info("Получен запрос на создание DocAttribute: {}", docAttributeCreateDto);
        return modelMapper.map(docAttribute, DocAttributeDto.class);
    }

    @Operation(summary = "Изменить атрибут")
    @RequestMapping(value = "/{docAttributeId}", method = RequestMethod.PATCH)
    @SecurityRequirement(name = "JWT")
    public DocAttributeDto updateAttribute(
            @PathVariable @Parameter(description = "ID атрибута") Long docAttributeId,
            @Valid @RequestBody @Parameter(description = "DTO изменения типа") DocAttributeUpdateRequestDto docAttributeUpdateRequestDto) {
        DocAttributeUpdateRequest docAttributeUpdateRequest =
                modelMapper.map(docAttributeUpdateRequestDto, DocAttributeUpdateRequest.class);
        DocAttribute docAttribute =
                docAttributeService.updateDocAttribute(docAttributeId, docAttributeUpdateRequest);

        log.info(
                "Получен запрос на обновление DocAttribute с ID: {}. Обновлённый DocAttribute: {}",
                docAttributeId,
                docAttribute);
        return modelMapper.map(docAttribute, DocAttributeDto.class);
    }

    @Operation(summary = "Поиск атрибута по подстроке в имени")
    @GetMapping("/name/{name}")
    @SecurityRequirement(name = "JWT")
    public List<DocAttributeDto> getDocAttributesByNameLike(
            @PathVariable @Parameter(description = "Подстрока имени") String name) {
        List<DocAttribute> docAttributes = docAttributeService.getDocAttributesByName(name);
        return docAttributes.stream()
                .map(o -> modelMapper.map(o, DocAttributeDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Удалить атрибут")
    @DeleteMapping("/{docAttributeId}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttribute(
            @PathVariable @Parameter(description = "ID атрибута") Long docAttributeId) {
        log.info("Получен запрос на удаление DocAttribute с ID: {}", docAttributeId);
        docAttributeService.deleteDocAttribute(docAttributeId);
    }

    private DocAttributeDto convertToDto(DocAttribute docAttribute) {
        return modelMapper.map(docAttribute, DocAttributeDto.class);
    }
}
