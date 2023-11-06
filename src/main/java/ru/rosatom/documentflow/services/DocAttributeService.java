package ru.rosatom.documentflow.services;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;

public interface DocAttributeService {
  Page<DocAttribute> getAllDocAttributes(Optional<Integer> page, Optional<String> sortBy);

  DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest);

  DocAttribute updateDocAttribute(
      Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest);

  void deleteDocAttribute(Long id);

  List<DocAttribute> getDocAttributesByName(String name);

  DocAttribute getDocAttributeById(Long id);
}
