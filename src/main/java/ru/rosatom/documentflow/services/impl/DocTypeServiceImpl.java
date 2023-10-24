package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocTypeService;

import java.util.List;
import java.util.Optional;

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
    throw new ObjectNotFoundException("DocTyp e with ID " + id + " not found.");
  }

  @Override
  public DocType createDocType(DocType docType) {
    return docTypeRepository.save(docType);
  }

  @Override
  public DocType updateDocType(Long id, DocType docType) {

    if (docTypeRepository.existsById(id)) {
      return docTypeRepository.save(docType);
    } else {
      throw new ObjectNotFoundException("DocType with ID " + id + " not found.");
    }
  }

  @Override
  public void deleteDocType(Long id) {
    if (docTypeRepository.existsById(id)) {
      docTypeRepository.deleteById(id);
    } else {
      throw new ObjectNotFoundException("DocType with ID " + id + " not found.");
    }
  }
}
