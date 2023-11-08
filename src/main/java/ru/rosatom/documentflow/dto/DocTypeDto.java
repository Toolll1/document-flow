package ru.rosatom.documentflow.dto;

import lombok.Data;
import ru.rosatom.documentflow.models.AgreementType;
import ru.rosatom.documentflow.models.DocAttribute;

import java.util.ArrayList;
import java.util.List;

@Data
public class DocTypeDto {
  private long id;
  private String name;
  private List<DocAttribute> attributes = new ArrayList<>();
  private AgreementType agreementType;
}
