package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocAttribute;

import java.util.List;

public interface DocAttributeService {
    List<DocAttribute> getAllDocAttributes();
    DocAttribute getDocAttributeById(Long id);
    DocAttribute createDocAttribute(DocAttribute docAttribute);
    DocAttribute updateDocAttribute(Long id, DocAttribute docAttribute);
    void deleteDocAttribute(Long id);
    List<DocAttribute> getDocAttributesByName(String name);
}
