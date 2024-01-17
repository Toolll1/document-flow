package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Schema(description = "Комментарий")
public class DocProcessCommentDto {

    @Schema(description = "ID комментария")
    private Long id;
    @Schema(description = "Текст комментария")
    private String content;
    @Schema(description = "Автор комментария")
    private UserReplyDto author;
    @Schema(description = "Дата создания комментария")
    private LocalDateTime createdAt;
    @Schema(description = "ID Документа")
    private Long documentId;
}
