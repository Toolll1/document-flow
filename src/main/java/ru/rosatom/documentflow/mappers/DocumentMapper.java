package ru.rosatom.documentflow.mappers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.dto.DocAttributeValueCreateDto;
import ru.rosatom.documentflow.dto.DocTypeDto;
import ru.rosatom.documentflow.dto.DocumentCreateDto;
import ru.rosatom.documentflow.dto.DocumentDto;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.DocTypeService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentMapper {

    final DocTypeService docTypeService;
    final ModelMapper modelMapper;
    final DocAttributeService docAttributeService;

    public Document documentFromCreateDto(DocumentCreateDto dto) {
        List<DocAttributeValues> attributeValues = new ArrayList<>();
        List<DocAttributeValueCreateDto> listDto = dto.getDocAttributeValueCreateDtos();
        for (DocAttributeValueCreateDto createDto : listDto) {
            DocAttributeValues values = new DocAttributeValues();
            values.setValue(createDto.getValue());
            values.setAttribute(docAttributeService.getDocAttributeById(createDto.getAttributeId()));
            attributeValues.add(values);
        }
        return Document.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .documentPath(dto.getDocumentPath())
                .date(dto.getDate())
                .idOrganization(dto.getIdOrganization())
                .docType(docTypeService.getDocTypeById(dto.getDocTypId()))
                .attributeValues(attributeValues)
                .build();
    }

    public DocumentDto documentToDto(Document document) {
        return DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .documentPath(document.getDocumentPath())
                .date(document.getDate())
                .idOrganization(document.getIdOrganization())
                .ownerId(document.getOwnerId())
                .docTypeDto(modelMapper.map(document.getDocType(), DocTypeDto.class))
                .attributeValues(document.getAttributeValues())
                .build();
    }
}
