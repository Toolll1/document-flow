package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;


public interface DocAttributeService {
    Page<DocAttribute> getAllDocAttributes(Integer page, Integer size, String sort);

    DocAttribute getDocAttributeById(Long id);

    DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest);

    DocAttribute updateDocAttribute(DocAttribute docAttribute);

    void deleteDocAttribute(Long id);
}
