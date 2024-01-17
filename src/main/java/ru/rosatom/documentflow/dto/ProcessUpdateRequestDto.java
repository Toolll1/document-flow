package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на обновление документа")
@Getter
@Setter
public class ProcessUpdateRequestDto {
    @Schema(description = "ID процесса", requiredMode = REQUIRED)
    private Long processId;
    @Schema(description = "Комментарий")
    private String comment;
}
