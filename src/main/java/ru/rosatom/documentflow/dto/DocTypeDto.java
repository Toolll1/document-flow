package ru.rosatom.documentflow.dto;

import lombok.Data;
import ru.rosatom.documentflow.models.AgreementType;

@Data
public class DocTypeDto {
    private long id;
    private String name;
    private AgreementType agreementType;
}
