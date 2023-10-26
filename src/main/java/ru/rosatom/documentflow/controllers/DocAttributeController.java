package ru.rosatom.documentflow.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocAttributeCreateDto;
import ru.rosatom.documentflow.dto.DocAttributeDto;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.services.DocAttributeService;

import javax.validation.Valid;

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
    public Page<DocAttributeDto> getAllDocAttributes(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort) {

        Page<DocAttribute> attributes = docAttributeService.getAllDocAttributes(page, size, sort);

        return attributes.map(attr -> modelMapper.map(attr, DocAttributeDto.class));
    }

    @GetMapping("/{docAttributeId}")
    public DocAttributeDto getAttribute(@PathVariable Long docAttributeId) {
        DocAttribute docAttribute = docAttributeService.getDocAttributeById(docAttributeId);
        log.info("Получен запрос на получение DocAttribute с ID: {}", docAttributeId);
        return convertToDto(docAttribute);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocAttributeDto createAttribute(@Valid @RequestBody DocAttributeCreateDto docAttributeCreateDto) {
        DocAttributeCreationRequest docAttributeCreationRequest = modelMapper.map(docAttributeCreateDto, DocAttributeCreationRequest.class);
        DocAttribute docAttribute = docAttributeService.createDocAttribute(docAttributeCreationRequest);
        log.info("Получен запрос на создание DocAttribute: {}", docAttributeCreateDto);
        return convertToDto(docAttribute);

    }

    @PutMapping("/{docAttributeId}")
    public DocAttributeDto updateAttribute(@Valid @RequestBody DocAttributeDto docAttributeDto) {
        DocAttribute docAttribute = convertToEntity(docAttributeDto);
        docAttribute = docAttributeService.updateDocAttribute(docAttribute);
        log.info(
                "Получен запрос на обновление DocAttribute с ID: {}. Обновлённый DocAttribute: {}",
                docAttributeDto.getId(),
                docAttributeDto);
        return convertToDto(docAttribute);
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

    private DocAttribute convertToEntity(DocAttributeDto docAttributeDto) {
        return modelMapper.map(docAttributeDto, DocAttribute.class);
    }
}
