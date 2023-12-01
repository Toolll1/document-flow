package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные об организации")
public class OrgDto {

    @Schema(description = "ID организации")
    private long id;

    @Schema(description = "Название")
    private String name;

    @Schema(description = "ИНН")
    private String inn;

    @Schema(description = "ID сотрудника, получающего по-умолчанию документы на согласование")
    private long userId;
}
