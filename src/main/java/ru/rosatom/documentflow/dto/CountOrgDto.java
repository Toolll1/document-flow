package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Schema(name = "Кол-во организаций")
public class CountOrgDto {

    @Schema(name = "Количество организаций")
    private final int countOrganization;
}
