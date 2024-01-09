package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Schema(description = "Параметры документа")
public class DocParams {

    @Schema(description = "Текст", requiredMode = REQUIRED)
    String text;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    @Schema(description = "ID создателя", requiredMode = REQUIRED)
    Long creatorId;
    @Schema(description = "ID типа", requiredMode = REQUIRED)
    Long typeId;
    @Schema(description = "ID атрибута", requiredMode = REQUIRED)
    Long attributeId;
    @Schema(description = "Значение атрибута", requiredMode = REQUIRED)
    String attributeValue;
}
