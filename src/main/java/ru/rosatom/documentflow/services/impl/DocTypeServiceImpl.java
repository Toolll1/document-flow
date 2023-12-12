package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocTypeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeServiceImpl implements DocTypeService {

    private final DocTypeRepository docTypeRepository;
    private final DocAttributeRepository docAttributeRepository;

    @Override
    public Page<DocType> getAllDocTypes(Pageable pageable, Optional<Long> orgId) {
        return orgId.map(id -> docTypeRepository.findAllByUserOrganization(id, pageable))
                .orElse(docTypeRepository.findAll(pageable));
    }

    @Override
    public DocType getDocTypeById(Long id) {
        return docTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Тип документа с ID " + id + " не найден."));
    }

    @Override
    public DocType createDocType(DocTypeCreationRequest docTypeCreationRequest) {
        DocType docType = DocType.builder()
                .name(docTypeCreationRequest.getName())
                .agreementType(docTypeCreationRequest.getAgreementType())
                .build();
        return docTypeRepository.save(docType);
    }

    @Override
    public DocType updateDocType(Long docTypeId, DocTypeUpdateRequest docTypeUpdateRequest) {
        DocType docType = getDocTypeById(docTypeId);
        docType.setName(
                Objects.requireNonNullElse(docTypeUpdateRequest.getName(), docType.getName()));

        return docTypeRepository.save(docType);
    }

    @Override
    public void deleteDocType(Long id) {
        DocType docType = getDocTypeById(id);
        docTypeRepository.delete(docType);
    }

    @Override
    public List<DocType> getDocTypesByName(String name, User user) {
        List<DocType> docTypes;
        if (user.getRole().equals(UserRole.ADMIN)) {
            docTypes = docTypeRepository.findByNameContains(name);
        } else {
            docTypes = docTypeRepository.findByUserOrganizationIdAndNameContains(user.getOrganization().getId(), name);
        }
        return docTypes;
    }

    @Override
    public DocType attributeToType(Long docTypeId, Long docAttributeId) {
        DocType docType = docTypeRepository.findById(docTypeId).get();
        DocAttribute docAttribute = docAttributeRepository.findById(docAttributeId).get();

        docType.addAttributes(docAttribute);

        return docTypeRepository.save(docType);
    }

    public boolean isAllowedType(Long id, User user) {
        boolean alllowed = Objects.equals(getDocTypeById(id).getUserOrganization().getId(), user.getOrganization().getId());
        return alllowed;
    }

    public boolean isAllowedTypeAttribute(Long docTypeId, Long docAttributeId, User user) {
        boolean alllowed = Objects.equals(getDocTypeById(docTypeId).getUserOrganization().getId(), user.getOrganization().getId()) &&
                Objects.equals(docAttributeRepository.findById(docAttributeId).get().getOrganization().getId(), user.getOrganization().getId());
        return alllowed;
    }
}
