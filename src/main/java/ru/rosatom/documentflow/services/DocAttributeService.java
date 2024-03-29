package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.models.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DocAttributeService {
    Page<DocAttribute> getAllDocAttributes(Pageable pageable, Optional<Long> otgId);

    DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest);

    DocAttribute updateDocAttribute(
            Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest);

    void deleteDocAttribute(Long id);

    List<DocAttribute> getDocAttributesByName(String name, User user);

    DocAttribute getDocAttributeById(Long id);

    Set<DocAttribute> getDocAttributesByIds(List<Long> ids);

    Set<DocAttribute> getAllByIdsElseThrow(List<Long> ids);

    List<DocAttribute> findAllByOrganizationId(Long orgId);
}
