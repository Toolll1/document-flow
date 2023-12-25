package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.models.User;

import java.util.List;
import java.util.Optional;

public interface DocAttributeService {
    Page<DocAttribute> getAllDocAttributes(Pageable pageable, Optional<Long> otgId);

    DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest);

    DocAttribute updateDocAttribute(
            Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest);

    void deleteDocAttribute(Long id);

    List<DocAttribute> getDocAttributesByName(String name, User user);

    DocAttribute getDocAttributeById(Long id);

    List<DocAttribute> getDocAttributesByIds(List<Long> ids);
}
