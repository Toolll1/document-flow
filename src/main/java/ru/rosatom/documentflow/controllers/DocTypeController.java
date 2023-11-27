package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocTypeCreateDto;
import ru.rosatom.documentflow.dto.DocTypeDto;
import ru.rosatom.documentflow.dto.DocTypeUpdateRequestDto;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;
import ru.rosatom.documentflow.services.DocTypeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
  List<DocTypeDto> getAllDocTypes(
      @RequestParam @Parameter(description = "Номер страницы") Optional<Integer> page,
      @RequestParam @Parameter(description = "Сортировка") Optional<String> sortBy) {
    return docTypeService.getAllDocTypes(page, sortBy).stream()
            .map(o -> modelMapper.map(o, DocTypeDto.class))
            .collect(Collectors.toList());
  }

  @Operation(summary = "Получить тип по ID")
  @GetMapping("/{docTypeId}")
  @SecurityRequirement(name = "JWT")
  @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
  public DocTypeDto getDocType(@PathVariable @Parameter(description = "ID типа") Long docTypeId) {
    DocType docType = docTypeService.getDocTypeById(docTypeId);
    log.info("Получен запрос на получение DocType с ID: {}", docTypeId);
    return modelMapper.map(docType, DocTypeDto.class);
  }

  @Operation(summary = "Создать тип")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @SecurityRequirement(name = "JWT")
  public DocTypeDto createDocType(@Valid @RequestBody @Parameter(description = "DTO создания типа") DocTypeCreateDto docTypeCreateDto) {
    DocTypeCreationRequest docTypeCreationRequest =
        modelMapper.map(docTypeCreateDto, DocTypeCreationRequest.class);
    DocType docType = docTypeService.createDocType(docTypeCreationRequest);
    log.info("Получен запрос на создание DocType: {}", docTypeCreateDto);

    return modelMapper.map(docType, DocTypeDto.class);
  }

  @Operation(summary = "Изменить тип")
  @RequestMapping(value = "/{docTypeId}", method = RequestMethod.PATCH)
  @SecurityRequirement(name = "JWT")
  public DocTypeDto updateDocType(
      @PathVariable @Parameter(description = "ID типа") Long docTypeId,
      @Valid @RequestBody @Parameter(description = "DTO изменения типа") DocTypeUpdateRequestDto docTypeUpdateRequestDto) {
    DocTypeUpdateRequest docTypeUpdateRequest =
        modelMapper.map(docTypeUpdateRequestDto, DocTypeUpdateRequest.class);
    DocType docType = docTypeService.updateDocType(docTypeId, docTypeUpdateRequest);

    log.info(
        "Получен запрос на обновление DocType с ID: {}. Обновлен DocType: {}", docTypeId, docType);
    return modelMapper.map(docType, DocTypeDto.class);
  }

  @Operation(summary = "Поиск типа по подстроке в имени")
  @GetMapping("/name/{name}")
  @SecurityRequirement(name = "JWT")
  @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
  public List<DocTypeDto> getDocTypesByNameLike(
      @PathVariable @Parameter(description = "Подстрока имени") String name) {
    List<DocType> docTypes = docTypeService.getDocTypesByName(name);
    return docTypes.stream()
        .map(o -> modelMapper.map(o, DocTypeDto.class))
        .collect(Collectors.toList());
  }

  @Operation(summary = "Добавить атрибут к типу")
  @RequestMapping(value = "/{docTypeId}/attributes/{docAttributeId}", method = RequestMethod.PUT)
  @SecurityRequirement(name = "JWT")
  public DocTypeDto addAttributeToType(
      @PathVariable @Parameter(description = "ID типа") Long docTypeId,
      @PathVariable @Parameter(description = "ID атрибута") Long docAttributeId) {
    log.info("Добавлен атрибут с ID: {} к документу с ID: {}", docAttributeId, docTypeId);

    return modelMapper.map(docTypeService.attributeToType(docTypeId, docAttributeId), DocTypeDto.class);
  }

  @Operation(summary = "Удалить тип")
  @DeleteMapping("/{docTypeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SecurityRequirement(name = "JWT")
  public void deleteDocType(@PathVariable @Parameter(description = "ID типа") Long docTypeId) {
    log.info("Получен запрос на удаление DocType с ID: {}", docTypeId);
    docTypeService.deleteDocType(docTypeId);
  }

    @Operation(summary = "Получить все типы", description = "Все типы с пагинацией и сортировкой")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    List<DocTypeDto> getAllDocTypes(
            @RequestParam @Parameter(description = "Номер страницы") Optional<Integer> page,
            @RequestParam @Parameter(description = "Сортировка") Optional<String> sortBy) {
        return docTypeService.getAllDocTypes(page, sortBy).stream()
                .map(o -> modelMapper.map(o, DocTypeDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получить тип по ID")
    @GetMapping("/{docTypeId}")
    @SecurityRequirement(name = "JWT")
    public DocTypeDto getDocType(@PathVariable @Parameter(description = "ID типа") Long docTypeId) {
        DocType docType = docTypeService.getDocTypeById(docTypeId);
        log.info("Получен запрос на получение DocType с ID: {}", docTypeId);
        return modelMapper.map(docType, DocTypeDto.class);
    }

    @Operation(summary = "Создать тип")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    public DocTypeDto createDocType(@Valid @RequestBody @Parameter(description = "DTO создания типа") DocTypeCreateDto docTypeCreateDto) {
        DocTypeCreationRequest docTypeCreationRequest =
                modelMapper.map(docTypeCreateDto, DocTypeCreationRequest.class);
        DocType docType = docTypeService.createDocType(docTypeCreationRequest);
        log.info("Получен запрос на создание DocType: {}", docTypeCreateDto);

        return modelMapper.map(docType, DocTypeDto.class);
    }

    @Operation(summary = "Изменить тип")
    @RequestMapping(value = "/{docTypeId}", method = RequestMethod.PATCH)
    @SecurityRequirement(name = "JWT")
    public DocTypeDto updateDocType(
            @PathVariable @Parameter(description = "ID типа") Long docTypeId,
            @Valid @RequestBody @Parameter(description = "DTO изменения типа") DocTypeUpdateRequestDto docTypeUpdateRequestDto) {
        DocTypeUpdateRequest docTypeUpdateRequest =
                modelMapper.map(docTypeUpdateRequestDto, DocTypeUpdateRequest.class);
        DocType docType = docTypeService.updateDocType(docTypeId, docTypeUpdateRequest);

        log.info(
                "Получен запрос на обновление DocType с ID: {}. Обновлен DocType: {}", docTypeId, docType);
        return modelMapper.map(docType, DocTypeDto.class);
    }

    @Operation(summary = "Поиск типа по подстроке в имени")
    @GetMapping("/name/{name}")
    @SecurityRequirement(name = "JWT")
    public List<DocTypeDto> getDocTypesByNameLike(
            @PathVariable @Parameter(description = "Подстрока имени") String name) {
        List<DocType> docTypes = docTypeService.getDocTypesByName(name);
        return docTypes.stream()
                .map(o -> modelMapper.map(o, DocTypeDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Добавить атрибут к типу")
    @RequestMapping(value = "/{docTypeId}/attributes/{docAttributeId}", method = RequestMethod.PUT)
    @SecurityRequirement(name = "JWT")
    public DocTypeDto addAttributeToType(
            @PathVariable @Parameter(description = "ID типа") Long docTypeId,
            @PathVariable @Parameter(description = "ID атрибута") Long docAttributeId) {
        log.info("Добавлен атрибут с ID: {} к документу с ID: {}", docAttributeId, docTypeId);

        return modelMapper.map(docTypeService.attributeToType(docTypeId, docAttributeId), DocTypeDto.class);
    }


    @Operation(summary = "Удалить тип")
    @DeleteMapping("/{docTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "JWT")
    public void deleteDocType(@PathVariable @Parameter(description = "ID типа") Long docTypeId) {
        log.info("Получен запрос на удаление DocType с ID: {}", docTypeId);
        docTypeService.deleteDocType(docTypeId);
    }

    private DocTypeDto convertToDto(DocType docType) {
        return modelMapper.map(docType, DocTypeDto.class);
    }
}
