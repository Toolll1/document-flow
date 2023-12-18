package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.rosatom.documentflow.models.AgreementType;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Тип документа")
public class DocTypeDto {
    @Schema(description = "ID типа документа")
    private long id;

    @Schema(description = "Наименование типа")
    private String name;

    @Schema(description = "Аттрибуты привязанные к типу")
    private List<DocAttributeDto> attributes = new ArrayList<>();


    @Schema(
            description =
                    "Для согласования документа требуется: EVERYONE - все получатели, ANYONE - хотя бы один, QUORUM - не менее 50% получателей.")
    private AgreementType agreementType;

    @Schema(description = "ID организации")
    private OrgDto userOrganization;

}
