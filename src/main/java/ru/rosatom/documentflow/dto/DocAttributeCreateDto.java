package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@Schema(description = "Создание атрибута")
public class DocAttributeCreateDto {

    @Schema(description = "Название атрибута", minLength = 1, maxLength = 255, requiredMode = REQUIRED)
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "Тип атрибута", minLength = 1, maxLength = 255, requiredMode = REQUIRED)
    @Size(min = 1, max = 255)
    private String type;

    @Schema(description = "ID организации атрибута", requiredMode = REQUIRED)
    private Long organizationId;

}
