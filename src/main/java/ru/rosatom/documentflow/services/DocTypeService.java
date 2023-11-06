package ru.rosatom.documentflow.services;

import java.util.List;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;

public interface DocTypeService {
  List<DocType> getAllDocTypes();

  DocType getDocTypeById(Long id);

  DocType createDocType(DocTypeCreationRequest docTypeCreationRequest);

  DocType updateDocType(Long docTypeId, DocTypeUpdateRequest docTypeUpdateRequest);

  void deleteDocType(Long id);

  List<DocType> getDocTypesByName(String name);
}
