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
@Schema(name = "Параметры документа")
public class DocParams {

    @Schema(name = "Текст")
    String text;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    @Schema(name = "ID оздателя")
    Long creatorId;
    @Schema(name = "ID типа")
    Long typeId;
    @Schema(name = "ID атрибута")
    Long attributeId;
    @Schema(name = "Значение атрибута")
    String attributeValue;
}
