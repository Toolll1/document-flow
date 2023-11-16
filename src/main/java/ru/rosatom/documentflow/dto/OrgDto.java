package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Данные об организации")
public class OrgDto {

    @Schema(name = "ID организации")
    private long id;

    @Schema(name = "Название")
    private String name;

    @Schema(name = "ИНН")
    private String inn;
}
