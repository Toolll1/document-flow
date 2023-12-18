package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Schema(description = "Параметры документа")
public class DocParams {

    @Schema(description = "Текст")
    String text;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    @Schema(description = "ID создателя")
    Long creatorId;
    @Schema(description = "ID типа")
    Long typeId;
    @Schema(description = "ID атрибута")
    Long attributeId;
    @Schema(description = "Значение атрибута")
    String attributeValue;
}
