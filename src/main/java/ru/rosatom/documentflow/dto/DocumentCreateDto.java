package ru.rosatom.documentflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.rosatom.documentflow.adapters.CommonUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(name = "Создание документа")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentCreateDto {
  @Schema(name = "ID документа")
  Long id;

  @Schema(name = "Название документа")
  @NotNull
  @NotEmpty
  String title;

  @Schema(name = "Путь документа")
  @NotNull
  @NotEmpty
  String documentPath;

  @Schema(name = "Дата создания")
  @NotNull
  @JsonFormat(pattern = CommonUtils.DATE_TIME_PATTERN)
  LocalDateTime date;

  @Schema(name = "ID организации")
  @NotNull
  Long idOrganization;

  @Schema(name = "Тип документа")
  @NotNull
  Long docTypId;

  @Schema(name = "Список атрибутов")
  @NotNull
  List<DocAttributeValueCreateDto> docAttributeValueCreateDtos;
}
