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
@Schema(description = "Изменения в документе")
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChangesDto {

    @Schema(description = "ID изменения")
    Long id;

    @Schema(description = "ID документа")
    Long documentId;

    @Schema(description = "Дата изменения")
    LocalDate dateChange;

    @Schema(description = "Изменения")
    String changes;

    @Schema(description = "Предыдущая версия")
    String previousVersion;

    @Schema(description = "ID автора изменения")
    Long userChangerId;
}
