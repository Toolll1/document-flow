package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@Schema(description = "Создание типа")
public class DocTypeCreateDto {

    @Schema(description = "Название типа", minLength = 1, maxLength = 255, requiredMode = REQUIRED)
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "Для согласования документа требуется: " +
            "EVERYONE - все получатели, " +
            "ANYONE - хотя бы один, " +
            "QUORUM - не менее 50% получателей.", requiredMode = REQUIRED, allowableValues = {"EVERYONE", "ANYONE", "QUORUM"})
    @Pattern(regexp = "^EVERYONE$|^ANYONE$|^QUORUM$")
    private String agreementType;

    @Schema(description = "Организация", requiredMode = REQUIRED)
    private Long organizationId;

    @Schema(description = "Список уникальных идентификаторов атрибутов", requiredMode = REQUIRED)
    private Set<Long> attributes = new HashSet<>();
}
