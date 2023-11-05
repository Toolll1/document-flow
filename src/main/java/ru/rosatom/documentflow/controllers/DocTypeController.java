package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.*;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.services.DocTypeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/doctypes")
@PreAuthorize("hasAuthority('ADMIN')")
public class DocTypeController {

  private final DocTypeService docTypeService;
  private final ModelMapper modelMapper;

//  @GetMapping
//  public List<DocTypeDto> getAllDocTypes() {
//    log.info("Получен запрос на получение всех типов документа");
//    return docTypeService.getAllDocTypes().stream().map(this::convertToDto).collect(Collectors.toList());
//  }

  @GetMapping
  public Page<DocTypeDto> getDocTypes(
                                      @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                      @RequestParam(value = "limit", defaultValue = "20") Integer limit){
    return
//    log.info("Received a request to search for all users for params: ids {}, sort {}, from {}, size {}", ids, sort, from, size);
//
//    return docTypeService.getDocTypes(ids, sort.toUpperCase(), from, size).stream()
//            .map(o -> modelMapper.map(o, DocTypeDto.class))
//            .collect(Collectors.toList());
  }

  @GetMapping("/{docTypeId}")
  public DocTypeDto getDocType(@PathVariable Long docTypeId) {
    DocType docType = docTypeService.getDocTypeById(docTypeId);
    log.info("Получен запрос на получение DocType с ID: {}", docTypeId);
    return convertToDto(docType);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DocTypeDto createDocType(@Valid @RequestBody DocTypeCreateDto docTypeCreateDto) {
    DocTypeCreationRequest docTypeCreationRequest =
        modelMapper.map(docTypeCreateDto, DocTypeCreationRequest.class);
    DocType docType = docTypeService.createDocType(docTypeCreationRequest);
    log.info("Получен запрос на создание DocType: {}", docTypeCreateDto);

    return convertToDto(docType);
  }

  @RequestMapping(value = "/{docTypeId}", method = RequestMethod.PATCH)
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

  @GetMapping("/name/{name}")
  public List<DocTypeDto> getOrgsByNameLike(@PathVariable String name) {
    List<DocType> docTypes = docTypeService.getDocTypesByName(name);
    return docTypes.stream()
            .map(o -> modelMapper.map(o, DocTypeDto.class))
            .collect(Collectors.toList());
  }

  @DeleteMapping("/{docTypeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDocType(@PathVariable Long docTypeId) {
    log.info("Получен запрос на удаление DocType с ID: {}", docTypeId);
    docTypeService.deleteDocType(docTypeId);
  }

  private DocTypeDto convertToDto(DocType docType) {
    return modelMapper.map(docType, DocTypeDto.class);
  }
}
