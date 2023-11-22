package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на создание организации")
public class OrgCreateRequestDto {

    @Schema(description = "Название", minLength = 1, maxLength = 255)
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "ИНН")
    @Pattern(regexp = "\\d{10}")
    private String inn;

}
