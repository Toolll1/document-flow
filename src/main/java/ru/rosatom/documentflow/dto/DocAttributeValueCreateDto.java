package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocAttributeValueCreateDto {
    @Schema(description = "ID атрибута", requiredMode = REQUIRED)
    @NotNull
    Long attributeId;

    @Schema(description = "Значение", requiredMode = REQUIRED)
    @NotNull
    @NotEmpty
    String value;
}
