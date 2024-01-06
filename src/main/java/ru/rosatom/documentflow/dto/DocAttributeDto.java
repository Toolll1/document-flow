package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Атрибут документа")
public class DocAttributeDto {
    @Schema(description = "ID атрибута", requiredMode = REQUIRED)
    private Long id;

    @Schema(description = "Наименование атрибута", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "Тип атрибута", requiredMode = REQUIRED)
    private String type;

    @Schema(description = "Организация атрибута", requiredMode = REQUIRED)
    private long organizationId;
}
