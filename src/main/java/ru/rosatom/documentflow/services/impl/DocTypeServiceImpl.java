package ru.rosatom.documentflow.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.AgreementType;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocTypeService;

@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeServiceImpl implements DocTypeService {

  private final DocTypeRepository docTypeRepository;
  private final DocAttributeRepository docAttributeRepository;

  @Override
  public Page<DocType> getAllDocTypes(Optional<Integer> page, Optional<String> sortBy) {
    return docTypeRepository.findAll(
        PageRequest.of(page.orElse(0), 20, Sort.Direction.ASC, sortBy.orElse("id")));
  }

  @Override
  public DocType getDocTypeById(Long id) {
    Optional<DocType> docType = docTypeRepository.findById(id);
    if (docType.isPresent()) {
      return docType.get();
    }
    throw new ObjectNotFoundException("Тип документа с ID " + id + " не найден.");
  }

  @Override
  public DocType createDocType(DocTypeCreationRequest docTypeCreationRequest) {
    DocType docType = DocType.builder().name(docTypeCreationRequest.getName()).build();
    return docTypeRepository.save(docType);
  }

  @Override
  public DocType updateDocType(Long docTypeId, DocTypeUpdateRequest docTypeUpdateRequest) {

    if (docTypeRepository.existsById(docTypeId)) {
      DocType docType = getDocTypeById(docTypeId);
      docType.setName(
          Objects.requireNonNullElse(docTypeUpdateRequest.getName(), docType.getName()));

      return docTypeRepository.save(docType);
    } else {
      throw new ObjectNotFoundException("Тип документа с ID " + docTypeId + " не найден.");
    }
  }

    @Override
    public DocType createDocType(DocTypeCreationRequest docTypeCreationRequest) {
        DocType docType = DocType.builder()
                .name(docTypeCreationRequest.getName())
                .agreementType(AgreementType.EVERYONE)
                .build();
        return docTypeRepository.save(docType);
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
