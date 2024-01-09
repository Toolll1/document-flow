package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на обновление документа")
public class ProcessUpdateRequestDto {
    @Schema(description = "ID процесса", requiredMode = REQUIRED)
    private Long processId;
    @Schema(description = "Комментарий")
    private String comment;
}
