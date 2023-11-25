package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocTypeCreationRequest {
    private String name;
    private AgreementType agreementType;
}
