package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Комментарий")
public class DocProcessCommentDto {
    @Schema(description = "Текст комментария")
    private String textComment;
    @Schema(description = "Автор комментария")
    private UserReplyDto authorComment;
    @Schema(description = "Дата создания комментария")
    private LocalDateTime date;
    @Schema(description = "Процесс по документу")
    private DocProcessDto docProcess;

}
