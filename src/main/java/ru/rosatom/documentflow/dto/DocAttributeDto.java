package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.rosatom.documentflow.models.UserOrganization;

@Data
@Schema(description = "Атрибут документа")
public class DocAttributeDto {
    @Schema(description = "ID атрибута")
    private Long id;

    @Schema(description = "Наименование атрибута")
    private String name;

    @Schema(description = "Тип атрибута")
    private String type;

    @Schema(description = "Организация атрибута")
    private UserOrganization userOrganization;
}
