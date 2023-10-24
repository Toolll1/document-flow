package ru.rosatom.documentflow.mappers;

import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.dto.DocumentChangesDto;
import ru.rosatom.documentflow.models.DocChanges;

@Service
public class DocumentChangesMapper {

    public DocChanges changesFromDto(DocumentChangesDto dto) {
        return DocChanges.builder()
                .id(dto.getId())
                .documentId(dto.getDocumentId())
                .dateChange(dto.getDateChange())
                .changes(dto.getChanges())
                .previousVersion(dto.getPreviousVersion())
                .userChangerId(dto.getUserChangerId())
                .userOwnerId(dto.getUserOwnerId())
                .build();
    }

    public DocumentChangesDto changesToDto(DocChanges docChanges) {
        return DocumentChangesDto.builder()
                .id(docChanges.getId())
                .documentId(docChanges.getDocumentId())
                .dateChange(docChanges.getDateChange())
                .changes(docChanges.getChanges())
                .previousVersion(docChanges.getPreviousVersion())
                .userChangerId(docChanges.getUserChangerId())
                .userOwnerId(docChanges.getUserOwnerId())
                .build();
    }
}
