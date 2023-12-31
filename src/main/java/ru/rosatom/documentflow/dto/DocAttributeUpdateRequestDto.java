package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Schema(description = "Изменение атрибута")
public class DocAttributeUpdateRequestDto {

    @Schema(description = "Название атрибута", minLength = 1, maxLength = 255)
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "Тип атрибута", minLength = 1, maxLength = 255)
    @Size(min = 1, max = 255)
    private String type;

}
