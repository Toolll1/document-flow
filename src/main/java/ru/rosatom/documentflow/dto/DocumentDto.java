package ru.rosatom.documentflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.rosatom.documentflow.adapters.CommonUtils;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.DocProcessStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Документ")
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    @Schema(description = "ID документа")
    Long id;

    @Schema(description = "Название")
    @NotNull
    @NotEmpty
    String name;

    @Schema(description = "Путь документа")
    @NotNull
    @NotEmpty
    String documentPath;

    @Schema(description = "Дата последнего обновления")
    @NotNull
    @JsonFormat(pattern = CommonUtils.DATE_TIME_PATTERN)
    LocalDateTime date;

    @Schema(description = "ID организации")
    @NotNull
    Long idOrganization;

    @Schema(description = "ID создателя")
    Long ownerId;

    @Schema(description = "Тип документа")
    @NotNull
    DocTypeDto docTypeDto;

    @Schema(description = "Список атрибутов")
    @NotNull
    List<DocAttributeValues> attributeValues;

    @Schema(description = "Статус")
    DocProcessStatus finalDocStatus;
}

