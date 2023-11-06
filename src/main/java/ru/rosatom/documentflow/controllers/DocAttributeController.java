package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocAttributeCreateDto;
import ru.rosatom.documentflow.dto.DocAttributeDto;
import ru.rosatom.documentflow.dto.DocAttributeUpdateRequestDto;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
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
public class DocAttributeController {

  private final DocAttributeService docAttributeService;
  private final ModelMapper modelMapper;

  @GetMapping
  public List<DocAttributeDto> getAllDocTypes() {
    log.info("Получен запрос на получение всех атрибутов документа");
    return docAttributeService.getAllDocAttributes().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @GetMapping("/{docAttributeId}")
  public DocAttributeDto getAttribute(@PathVariable Long docAttributeId) {
    DocAttribute docAttribute = docAttributeService.getDocAttributeById(docAttributeId);
    log.info("Получен запрос на получение DocAttribute с ID: {}", docAttributeId);
    return convertToDto(docAttribute);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DocAttributeDto createAttribute(
      @Valid @RequestBody DocAttributeCreateDto docAttributeCreateDto) {
    DocAttributeCreationRequest docAttributeCreationRequest =
        modelMapper.map(docAttributeCreateDto, DocAttributeCreationRequest.class);
    DocAttribute docAttribute = docAttributeService.createDocAttribute(docAttributeCreationRequest);
    log.info("Получен запрос на создание DocAttribute: {}", docAttributeCreateDto);
    return convertToDto(docAttribute);
  }

  @RequestMapping(value = "/{docAttributeId}", method = RequestMethod.PATCH)
  public DocAttributeDto updateAttribute(
      @PathVariable Long docAttributeId,
      @Valid @RequestBody DocAttributeUpdateRequestDto docAttributeUpdateRequestDto) {
    DocAttributeUpdateRequest docAttributeUpdateRequest =
        modelMapper.map(docAttributeUpdateRequestDto, DocAttributeUpdateRequest.class);
    DocAttribute docAttribute =
        docAttributeService.updateDocAttribute(docAttributeId, docAttributeUpdateRequest);

    log.info(
        "Получен запрос на обновление DocAttribute с ID: {}. Обновлённый DocAttribute: {}",
        docAttributeId,
        docAttribute);
    return convertToDto(docAttribute);
  }

  @GetMapping("/name/{name}")
  public List<DocAttributeDto> getDocAttributesByNameLike(@PathVariable String name) {
    List<DocAttribute> docAttributes = docAttributeService.getDocAttributesByName(name);
    return docAttributes.stream()
        .map(o -> modelMapper.map(o, DocAttributeDto.class))
        .collect(Collectors.toList());
  }

  @DeleteMapping("/{docAttributeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAttribute(@PathVariable Long docAttributeId) {
    log.info("Получен запрос на удаление DocAttribute с ID: {}", docAttributeId);
    docAttributeService.deleteDocAttribute(docAttributeId);
  }

  private DocAttributeDto convertToDto(DocAttribute docAttribute) {
    return modelMapper.map(docAttribute, DocAttributeDto.class);
  }
}
