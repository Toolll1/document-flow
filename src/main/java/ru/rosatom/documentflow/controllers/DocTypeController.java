package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.*;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.services.DocTypeService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/doctypes")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Тип документа")
public class DocTypeController {

  private final DocTypeService docTypeService;
  private final ModelMapper modelMapper;

  @Operation(summary = "Получить все типы", description = "Все типы с пагинацией и сортировкой")
  @GetMapping
  @SecurityRequirement(name = "JWT")
  List<DocTypeDto> getAllDocTypes(
      @RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
    return docTypeService.getAllDocTypes(page, sortBy).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Operation(summary = "Получить тип по ID")
  @GetMapping("/{docTypeId}")
  @SecurityRequirement(name = "JWT")
  public DocTypeDto getDocType(@PathVariable Long docTypeId) {
    DocType docType = docTypeService.getDocTypeById(docTypeId);
    log.info("Получен запрос на получение DocType с ID: {}", docTypeId);
    return convertToDto(docType);
  }

  @Operation(summary = "Создать тип")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @SecurityRequirement(name = "JWT")
  public DocTypeDto createDocType(@Valid @RequestBody DocTypeCreateDto docTypeCreateDto) {
    DocTypeCreationRequest docTypeCreationRequest =
        modelMapper.map(docTypeCreateDto, DocTypeCreationRequest.class);
    DocType docType = docTypeService.createDocType(docTypeCreationRequest);
    log.info("Получен запрос на создание DocType: {}", docTypeCreateDto);

    return convertToDto(docType);
  }

  @Operation(summary = "Изменить тип")
  @RequestMapping(value = "/{docTypeId}", method = RequestMethod.PATCH)
  @SecurityRequirement(name = "JWT")
  public DocTypeDto updateDocType(
      @PathVariable Long docTypeId,
      @Valid @RequestBody DocTypeUpdateRequestDto docTypeUpdateRequestDto) {
    DocTypeUpdateRequest docTypeUpdateRequest =
        modelMapper.map(docTypeUpdateRequestDto, DocTypeUpdateRequest.class);
    DocType docType = docTypeService.updateDocType(docTypeId, docTypeUpdateRequest);

    log.info(
        "Получен запрос на обновление DocType с ID: {}. Обновлен DocType: {}", docTypeId, docType);
    return convertToDto(docType);
  }

  @Operation(summary = "Поиск типа по подстроке в имени")
  @GetMapping("/name/{name}")
  @SecurityRequirement(name = "JWT")
  public List<DocTypeDto> getDocTypesByNameLike(@PathVariable String name) {
    List<DocType> docTypes = docTypeService.getDocTypesByName(name);
    return docTypes.stream()
        .map(o -> modelMapper.map(o, DocTypeDto.class))
        .collect(Collectors.toList());
  }

  @Operation(summary = "Добавить атрибут к типу")
  @RequestMapping(value = "/{docTypeId}/attributes/{docAttributeId}", method = RequestMethod.PUT)
  @SecurityRequirement(name = "JWT")
  public DocTypeDto addAttributeToType(
      @PathVariable Long docTypeId, @PathVariable Long docAttributeId) {
    log.info("Добавлен атрибут с ID: {} к документу с ID: {}", docAttributeId, docTypeId);

    return convertToDto(docTypeService.attributeToType(docTypeId, docAttributeId));
  }

  @Operation(summary = "Удалить тип")
  @DeleteMapping("/{docTypeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SecurityRequirement(name = "JWT")
  public void deleteDocType(@PathVariable Long docTypeId) {
    log.info("Получен запрос на удаление DocType с ID: {}", docTypeId);
    docTypeService.deleteDocType(docTypeId);
  }

  private DocTypeDto convertToDto(DocType docType) {
    return modelMapper.map(docType, DocTypeDto.class);
  }
}
