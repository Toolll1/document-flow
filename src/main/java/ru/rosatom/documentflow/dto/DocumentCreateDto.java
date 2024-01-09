package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@Schema(description = "Создание документа")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentCreateDto {

    @Schema(description = "Тип документа", requiredMode = REQUIRED)
    @NotNull
    Long docTypeId;

    @Schema(description = "Пользовательское название документа", requiredMode = REQUIRED)
    String title;

    @Schema(description = "Список атрибутов", requiredMode = REQUIRED)
    @NotNull
    List<DocAttributeValueCreateDto> attributeValues;
}
