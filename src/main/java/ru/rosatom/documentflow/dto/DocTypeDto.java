package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.rosatom.documentflow.models.AgreementType;

import java.util.HashSet;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Тип документа")
public class DocTypeDto {
    @Schema(description = "ID типа документа", requiredMode = REQUIRED)
    private long id;

    @Schema(description = "Наименование типа", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "Аттрибуты привязанные к типу", requiredMode = REQUIRED)
    private Set<DocAttributeDto> attributes = new HashSet<>();

    @Schema(description = "Для согласования документа требуется: " +
            "EVERYONE - все получатели, " +
            "ANYONE - хотя бы один, " +
            "QUORUM - не менее 50% получателей.", allowableValues = {"EVERYONE", "ANYONE", "QUORUM"}, requiredMode = REQUIRED)
    private AgreementType agreementType;

    @Schema(description = "ID организации", requiredMode = REQUIRED)
    private long organizationId;

    @Schema(description = "Архивный тип документа", requiredMode = REQUIRED)
    private boolean isArchived;

}
