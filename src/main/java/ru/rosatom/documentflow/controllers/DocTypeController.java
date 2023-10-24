package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocTypeCreateDto;
import ru.rosatom.documentflow.dto.DocTypeDto;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.services.DocTypeService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/doctypes")
public class DocTypeController {

  private final DocTypeService docTypeService;
  ModelMapper modelMapper;

  @GetMapping
  public List<DocTypeDto> getAllDocTypes() {
    List<DocType> docTypes = docTypeService.getAllDocTypes();
    return docTypes.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/{docTypeId}")
  public DocTypeDto getDocType(@PathVariable Long docTypeId) {
    DocType docType = docTypeService.getDocTypeById(docTypeId);
    log.info("Request received to get DocType with ID: {}", docTypeId);
    return convertToDto(docType);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DocTypeDto createDocType(@Valid @RequestBody DocTypeCreateDto docTypeCreateDto) {
    DocTypeCreationRequest docTypeCreationRequest =
        modelMapper.map(docTypeCreateDto, DocTypeCreationRequest.class);
    DocType docType = docTypeService.createDocType(docTypeCreationRequest);
    log.info("Request received to create a DocType: {}", docTypeCreateDto);

    return convertToDto(docType);
  }

  @PutMapping("/{docTypeId}")
  public DocTypeDto updateDocType(@Valid @RequestBody DocTypeDto docTypeDto) {
    DocType docType = convertToEntity(docTypeDto);
    docType = docTypeService.updateDocType(docType);

    log.info(
        "Request received to update DocType with ID: {}. Updated DocType: {}",
        docTypeDto.getId(),
        docTypeDto);
    return convertToDto(docType);
  }

  @DeleteMapping("/{docTypeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDocType(@PathVariable Long docTypeId) {
    log.info("Request received to delete DocType with ID: {}", docTypeId);
    docTypeService.deleteDocType(docTypeId);
  }

  private DocTypeDto convertToDto(DocType docType) {
    return modelMapper.map(docType, DocTypeDto.class);
  }

  private DocType convertToEntity(DocTypeDto docTypeDto) {
    return modelMapper.map(docTypeDto, DocType.class);
  }
}