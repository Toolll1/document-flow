package ru.rosatom.documentflow.mappers;

import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocumentDto;
import ru.rosatom.documentflow.models.Document;

@Service
public class DocumentMapper {

    public Document documentFromDto(DocumentDto dto) {
        return Document.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .documentPath(dto.getDocumentPath())
                .date(dto.getDate())
                .idOrganization(dto.getIdOrganization())
                .ownerId(dto.getOwnerId())
                .docType(dto.getDocType())
                .attributeValues(dto.getAttributeValues())
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
                .docType(document.getDocType())
                .attributeValues(document.getAttributeValues())
                .build();
    }
}
