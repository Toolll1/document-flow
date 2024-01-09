package ru.rosatom.documentflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.rosatom.documentflow.adapters.CommonUtils;
import ru.rosatom.documentflow.models.DocAttributeValues;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Документ")
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    @Schema(description = "ID документа", requiredMode = REQUIRED)
    private Long id;

    @Schema(description = "Название", requiredMode = REQUIRED)
    @NotNull
    @NotEmpty
    private String name;

    @Schema(description = "Пользовательское название документа", requiredMode = REQUIRED)
    private String title;

    @Schema(description = "Путь документа", requiredMode = REQUIRED)
    @NotNull
    @NotEmpty
    private String documentPath;

    @Schema(description = "Дата последнего обновления", requiredMode = REQUIRED)
    @NotNull
    @JsonFormat(pattern = CommonUtils.DATE_TIME_PATTERN)
    private LocalDateTime date;

    @Schema(description = "ID организации", requiredMode = REQUIRED)
    @NotNull
    private Long idOrganization;

    @Schema(description = "ID создателя", requiredMode = REQUIRED)
    private Long ownerId;

    @Schema(description = "Тип документа", requiredMode = REQUIRED)
    @NotNull
    private String docTypeName;

    @Schema(description = "Список атрибутов", requiredMode = REQUIRED)
    @NotNull
    private List<DocAttributeValues> attributeValues;

    @Schema(description = "Статус", requiredMode = REQUIRED)
    private  String finalDocStatus;

    @Schema(description = "Комментарии", requiredMode = REQUIRED)
    private List<DocProcessCommentDto> comments;
}

