package ru.rosatom.documentflow.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocTypeService;

@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeServiceImpl implements DocTypeService {

  private final DocTypeRepository docTypeRepository;

  @Override
  public List<DocType> getAllDocTypes() {
    return docTypeRepository.findAll();
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
  public void deleteDocType(Long id) {
    if (docTypeRepository.existsById(id)) {
      docTypeRepository.deleteById(id);
    } else {
      throw new ObjectNotFoundException("Тип документа с ID " + id + " не найден.");
    }
  }

  @Override
  public List<DocType> getDocTypesByName(String name) {
    return docTypeRepository.findByNameContains(name);
  }
}
