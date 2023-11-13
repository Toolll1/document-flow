package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Статистика по документам")
public class DocStatisticDTO {

    @Schema(name = "Количество")
    private long count;
}
