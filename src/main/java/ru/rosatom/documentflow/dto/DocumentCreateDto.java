package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Schema(description = "Создание документа")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentCreateDto {

    @Schema(description = "Тип документа")
    @NotNull
    Long docTypId;

    @Schema(description = "Список атрибутов")
    @NotNull
    List<DocAttributeValueCreateDto> docAttributeValueCreateDtos;

}
