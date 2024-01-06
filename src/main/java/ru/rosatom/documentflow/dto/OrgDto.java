package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Данные об организации")
public class OrgDto {

    @Schema(description = "ID организации", requiredMode = REQUIRED)
    private long id;

    @Schema(description = "Название", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "ИНН", requiredMode = REQUIRED)
    private String inn;

    @Schema(description = "ID сотрудника, получающего по-умолчанию документы на согласование", requiredMode = REQUIRED)
    private Long defaultRecipient;
}
