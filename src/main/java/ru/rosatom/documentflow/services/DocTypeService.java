package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface DocTypeService {
    Page<DocType> getAllDocTypes(Optional<Integer> page, Optional<String> sortBy);

    DocType getDocTypeById(Long id);

    DocType createDocType(DocTypeCreationRequest docTypeCreationRequest);

    DocType updateDocType(Long docTypeId, DocTypeUpdateRequest docTypeUpdateRequest);

    void deleteDocType(Long id);

    List<DocType> getDocTypesByName(String name);

    DocType attributeToType(Long docTypeId, Long docAttributeId);
}
