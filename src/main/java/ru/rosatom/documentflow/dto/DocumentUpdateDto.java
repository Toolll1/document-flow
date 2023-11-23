package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Обновление документа")
public class DocumentUpdateDto {
    @Schema(description = "Название")
    @Nullable
    String name;

    @Schema(description = "Путь документа")
    @Nullable
    String documentPath;

    @Schema(description = "Дата обновления")
    @Nullable
    LocalDateTime date;

    @Schema(description = "ID типа")
    @Nullable
    Long docTypeId;

    @Schema(description = "Предыдущая версия")
    @Nullable
    String previousVersion;

    @Schema(description = "Список атрибутов")
    @Nullable
    List<DocAttributeValueCreateDto> attributeValues;
}