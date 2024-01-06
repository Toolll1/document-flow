package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocTypeCreationRequest {
    private String name;
    private AgreementType agreementType;
    private Long organizationId;
    private List<Long> attributes = new ArrayList<>();
}
