package ru.rosatom.documentflow.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentChangesDto {

    Long id;
    Long documentId;
    LocalDate dateChange;
    String changes;
    String previousVersion;
    Long userChangerId;
}
