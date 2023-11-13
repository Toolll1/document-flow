package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "Процесс изменения документа")
public class DocProcessDto {

    @Schema(name = "ID процесса")
    private Long id;

    @Schema(name = "Документ")
    private Long document;

    @Schema(name = "Отправитель")
    private Long sender;

    @Schema(name = "Получатель")
    private Long recipient;

    @Schema(name = "Статус")
    private String status;

    @Schema(name = "Комментарий")
    private String comment;
}
