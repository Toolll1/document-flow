package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.UserOrganizationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocAttributeServiceImpl implements DocAttributeService {

    private final DocAttributeRepository docAttributeRepository;

    private final UserOrganizationService userOrganizationService;

    @Override
    public Page<DocAttribute> getAllDocAttributes(Pageable pageable, Optional<Long> orgId) {
        return orgId.map(id -> docAttributeRepository.findAllByUserOrganization(id, pageable))
                .orElse(docAttributeRepository.findAll(pageable));
    }

    @Override
    public DocAttribute getDocAttributeById(Long id) {
        return docAttributeRepository
                .findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("DocAttribute c ID " + id + " не найден."));
    }

    @Override
    public DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest) {
        DocAttribute docAttribute =
                DocAttribute.builder()
                        .name(docAttributeCreationRequest.getName())
                        .type(docAttributeCreationRequest.getType())
                        .organization(userOrganizationService.getOrganization(docAttributeCreationRequest.getOrganizationId()))
                        .build();
        return docAttributeRepository.save(docAttribute);
    }

    @Override
    public DocAttribute updateDocAttribute(
            Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest) {
        DocAttribute docAttribute = getDocAttributeById(docAttributeId);
        docAttribute.setName(
                Objects.requireNonNullElse(docAttributeUpdateRequest.getName(), docAttribute.getName()));
        docAttribute.setType(
                Objects.requireNonNullElse(docAttributeUpdateRequest.getType(), docAttribute.getType()));
        return docAttributeRepository.save(docAttribute);
    }

    @Override
    public void deleteDocAttribute(Long id) {
        docAttributeRepository.delete(getDocAttributeById(id));
    }

    @Override
    public List<DocAttribute> getDocAttributesByName(String name, User user) {
        List<DocAttribute> docAttributes;
        if (user.getRole().equals(UserRole.ADMIN)) {
            docAttributes = docAttributeRepository.findByNameContains(name);
        } else {
            docAttributes = docAttributeRepository.findByOrganizationIdAndNameContains(user.getOrganization().getId(), name);
        }
        return docAttributes;
    }

     public boolean isAllowedAttribute(Long id, User user) {
        boolean alllowed = Objects.equals(getDocAttributeById(id).getOrganization().getId(), user.getOrganization().getId());
        return alllowed;
    }
}
