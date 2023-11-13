package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(name = "Изменения в документе")
public class DocumentChangesDto {

    @Schema(name = "ID изменения")
    Long id;

    @Schema(name = "ID документа")
    Long documentId;

    @Schema(name = "Дата изменения")
    LocalDate dateChange;

    @Schema(name = "Изменения")
    String changes;

    @Schema(name = "Предыдущая версия")
    String previousVersion;

    @Schema(name = "ID автора изменения")
    Long userChangerId;
}
