package ru.rosatom.documentflow.controllers;

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
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocTypeService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/doctypes")
@PreAuthorize("hasAuthority('ADMIN')")
public class DocTypeController {

  private final DocTypeService docTypeService;
  private final ModelMapper modelMapper;
  private final DocTypeRepository docTypeRepository;
  private final DocAttributeRepository docAttributeRepository;



  @GetMapping
  List<DocTypeDto> getAllDocTypes(
      @RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
    return docTypeService.getAllDocTypes(page, sortBy).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
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
  public List<DocTypeDto> getDocTypesByNameLike(@PathVariable String name) {
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
  @RequestMapping(value = "/{docTypeId}/attributes/{docAttributeId}", method = RequestMethod.PUT)
//  @PutMapping("/{docTypeId}/attributes/{docAttributeId}")
  public DocType addAttributeToType(@PathVariable Long docTypeId,
                                    @PathVariable Long docAttributeId) {
    DocType docType = docTypeRepository.findById(docTypeId).get();
    DocAttribute docAttribute = docAttributeRepository.findById(docAttributeId).get();
    docType.addAttributes(docAttribute);
    return docTypeRepository.save(docType);
  }

  private DocTypeDto convertToDto(DocType docType) {
    return modelMapper.map(docType, DocTypeDto.class);
  }
}
