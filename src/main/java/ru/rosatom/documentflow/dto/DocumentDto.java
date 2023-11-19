package ru.rosatom.documentflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.rosatom.documentflow.adapters.CommonUtils;
import ru.rosatom.documentflow.models.DocAttributeValues;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(name = "Документ")
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
  @Schema(name = "ID документа")
  Long id;

  @Schema(name = "Название")
  @NotNull
  @NotEmpty
  String name;

  @Schema(name = "Путь документа")
  @NotNull
  @NotEmpty
  String documentPath;

  @Schema(name = "Дата последнего обновления")
  @NotNull
  @JsonFormat(pattern = CommonUtils.DATE_TIME_PATTERN)
  LocalDateTime date;

  @Schema(name = "ID организации")
  @NotNull
  Long idOrganization;

  @Schema(name = "ID создателя")
  Long ownerId;

  @Schema(name = "Тип документа")
  @NotNull
  String docTypeName;

  @Schema(name = "Список атрибутов")
  @NotNull
  List<DocAttributeValues> attributeValues;

  @Schema(name = "Статус")
  String finalDocStatus;
}

