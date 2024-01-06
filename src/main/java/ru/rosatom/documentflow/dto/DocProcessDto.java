package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@Schema(description = "Процесс изменения документа")
public class DocProcessDto {

    @Schema(description = "ID процесса", requiredMode = REQUIRED)
    private Long id;

    @Schema(description = "Документ", requiredMode = REQUIRED)
    private Long document;

    @Schema(description = "Отправитель", requiredMode = REQUIRED)
    private Long sender;

    @Schema(description = "Получатель", requiredMode = REQUIRED)
    private Long recipientUserId;

    @Schema(description = "Организация получатель", requiredMode = REQUIRED)
    private Long recipientOrganizationId;

    @Schema(description = "Статус", requiredMode = REQUIRED)
    private String status;

    @Schema(description = "Комментарий", requiredMode = REQUIRED)
    private String comment;
}
