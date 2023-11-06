package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;
import ru.rosatom.documentflow.models.User;

import java.util.List;

public interface DocTypeService {
  List<DocType> getAllDocTypes();

  DocType getDocTypeById(Long id);

  DocType createDocType(DocTypeCreationRequest docTypeCreationRequest);

  DocType updateDocType(Long docTypeId, DocTypeUpdateRequest docTypeUpdateRequest);

  void deleteDocType(Long id);

  List<DocType> getDocTypesByName(String name);
}
