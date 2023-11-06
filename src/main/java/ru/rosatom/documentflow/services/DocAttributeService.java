package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.models.DocType;

import java.util.List;


public interface DocAttributeService {
    List<DocAttribute>getAllDocAttributes();

    DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest);

    DocAttribute updateDocAttribute(Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest);

    void deleteDocAttribute(Long id);

    List<DocAttribute> getDocAttributesByName(String name);
    DocAttribute getDocAttributeById(Long id);
    //    Page<DocAttribute> getAllDocAttributes(Integer page, Integer size, String sort);
}
