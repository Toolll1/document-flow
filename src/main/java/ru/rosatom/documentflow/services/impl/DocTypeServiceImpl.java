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

@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeServiceImpl implements DocTypeService {

    private final DocTypeRepository docTypeRepository;
    private final DocAttributeRepository docAttributeRepository;

    @Override
    public Page<DocType> getAllDocTypes(Pageable pageable, User user) {
        return user.getRole().getAuthority().equals("USER")
                ? docTypeRepository.findAllByUserOrganization(user.getOrganization(), pageable)
                : docTypeRepository.findAll(pageable);
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
    public List<DocType> getDocTypesByName(String name) {
        return docTypeRepository.findByNameContains(name);
    }

    @Override
    public DocType attributeToType(Long docTypeId, Long docAttributeId) {
        DocType docType = docTypeRepository.findById(docTypeId).get();
        DocAttribute docAttribute = docAttributeRepository.findById(docAttributeId).get();

        docType.addAttributes(docAttribute);

        return docTypeRepository.save(docType);
    }
}
