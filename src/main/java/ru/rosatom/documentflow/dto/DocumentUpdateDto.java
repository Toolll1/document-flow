package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(name = "Обновление документа")
public class DocumentUpdateDto {
  @Schema(name = "Название")
  @Nullable
  String title;

  @Schema(name = "Путь документа")
  @Nullable
  String documentPath;

  @Schema(name = "Дата обновления")
  @Nullable
  LocalDateTime date;

  @Schema(name = "ID типа")
  @Nullable
  Long docTypeId;

  @Schema(name = "Предыдущая версия")
  @Nullable
  String previousVersion;

  @Schema(name = "Список атрибутов")
  @Nullable
  List<DocAttributeValueCreateDto> attributeValues = new ArrayList<>();
}