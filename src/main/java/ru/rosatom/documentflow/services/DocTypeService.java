package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;

import java.util.List;

public interface DocTypeService {
    List<DocType> getAllDocTypes();

    DocType getDocTypeById(Long id);

    DocType createDocType(DocTypeCreationRequest docTypeCreationRequest);

    DocType updateDocType(DocType docType);

    void deleteDocType(Long id);
}
