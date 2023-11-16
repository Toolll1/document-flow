package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "Запрос на обновление документа")
public class ProcessUpdateRequestDto {
    @Schema(name = "ID процесса")
    private Long processId;
    @Schema(name = "Комментарий")
    private String comment;
}
