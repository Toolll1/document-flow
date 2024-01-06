package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Изменения в документе")
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChangesDto {

    @Schema(description = "ID изменения", requiredMode = REQUIRED)
    private Long id;

    @Schema(description = "ID документа", requiredMode = REQUIRED)
    private Long documentId;

    @Schema(description = "Дата изменения", requiredMode = REQUIRED)
    private LocalDate dateChange;

    @Schema(description = "Изменения", requiredMode = REQUIRED)
    private String changes;

    @Schema(description = "Предыдущая версия", requiredMode = REQUIRED)
    private String previousVersion;

    @Schema(description = "ID автора изменения", requiredMode = REQUIRED)
    private Long userChangerId;
}
