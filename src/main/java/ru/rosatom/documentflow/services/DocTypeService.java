package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocType;

import java.util.List;

public interface DocTypeService {
    List<DocType> getAllDocTypes();

    DocType getDocTypeById(Long id);

    DocType createDocType(DocType docType);

    DocType updateDocType(Long id, DocType docType);

    void deleteDocType(Long id);
}
