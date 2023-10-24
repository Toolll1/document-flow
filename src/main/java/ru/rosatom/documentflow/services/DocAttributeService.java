package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;

import javax.print.Doc;
import java.util.List;

public interface DocAttributeService {
    List<DocAttribute> getAllDocAttributes();
    DocAttribute getDocAttributeById(Long id);
    DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest);
    DocAttribute updateDocAttribute(DocAttribute docAttribute);
    void deleteDocAttribute(Long id);
    List<DocAttribute> getDocAttributesByName(String name);
}
