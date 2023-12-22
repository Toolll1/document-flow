package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Изменение типа")
public class DocTypeUpdateRequestDto {

    @Schema(description = "Название типа", minLength = 1, maxLength = 255)
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "Список идентификаторов атрибутов")
    private List<Long> attributes = new ArrayList<>();
}
