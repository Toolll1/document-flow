package ru.rosatom.e2e.docType;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.organization.Organization;


@Data
@NoArgsConstructor
public class DocType {
    private Integer id;
    private String name;
    //private List<Attribute> attributes = new ArrayList<>();
    private AgreementType agreementType;
    private Organization organization;
}
