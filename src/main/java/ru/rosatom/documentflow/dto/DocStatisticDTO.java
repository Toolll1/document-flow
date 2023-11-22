package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Статистика по документам")
public class DocStatisticDTO {

  @Schema(description = "Количество")
  private long count;
}
