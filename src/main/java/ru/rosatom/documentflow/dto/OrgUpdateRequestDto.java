package ru.rosatom.documentflow.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на обновление организации")
public class OrgUpdateRequestDto {

    @Schema(description = "Название", minLength = 1, maxLength = 255)
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "ID сотрудника, получающего по-умолчанию документы на согласование")
    private Long userId;
}
