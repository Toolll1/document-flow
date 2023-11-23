package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Процесс изменения документа")
public class DocProcessDto {

  @Schema(description = "ID процесса")
  private Long id;

  @Schema(description = "Документ")
  private Long document;

  @Schema(description = "Отправитель")
  private Long sender;

  @Schema(description = "Получатель")
  private Long recipient;

  @Schema(description = "Статус")
  private String status;

  @Schema(description = "Комментарии")
  private List<DocProcessCommentDto> comment;
}
