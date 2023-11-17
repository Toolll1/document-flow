package ru.rosatom.documentflow.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReasonChangesInDocumentDto {
    private List<String> documentChanges;
}
