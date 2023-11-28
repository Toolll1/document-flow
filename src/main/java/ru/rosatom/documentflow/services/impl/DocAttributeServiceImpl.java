package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.UserOrganizationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DocAttributeServiceImpl implements DocAttributeService {

    private final DocAttributeRepository docAttributeRepository;

    private final UserOrganizationService userOrganizationService;

    @Override
    public Page<DocAttribute> getAllDocAttributes(Pageable pageable, User user) {
        return user.getRole().getAuthority().equals("USER") ?
                docAttributeRepository.findAllByUserOrganization(user.getOrganization(), pageable) :
                docAttributeRepository.findAll(pageable);
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
                        .userOrganization(userOrganizationService.getOrganization(docAttributeCreationRequest.getOrgId()))
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
    public List<DocAttribute> getDocAttributesByName(String name) {
        return docAttributeRepository.findByNameContains(name);
    }


}
