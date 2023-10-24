package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocAttributeServiceImpl implements DocAttributeService {

  private final DocAttributeRepository docAttributeRepository;

  @Override
  public List<DocAttribute> getAllDocAttributes() {
    return docAttributeRepository.findAll();
  }

  @Override
  public DocAttribute getDocAttributeById(Long id) {
    Optional<DocAttribute> docAttribute = docAttributeRepository.findById(id);
    if (docAttribute.isPresent()) {
      return docAttribute.get();
    }
    throw new ObjectNotFoundException("DocAttribute with ID " + id + " not found.");
  }

  @Override
  public DocAttribute createDocAttribute(DocAttribute docAttribute) {
    return docAttributeRepository.save(docAttribute);
  }

  @Override
  public DocAttribute updateDocAttribute(Long id, DocAttribute docAttribute) {
    if (docAttributeRepository.existsById(id)) {
      return docAttributeRepository.save(docAttribute);
    } else {
      throw new ObjectNotFoundException("DocAttribute with ID " + id + " not found.");
    }
  }

  @Override
  public void deleteDocAttribute(Long id) {
    if (docAttributeRepository.existsById(id)) {
      docAttributeRepository.deleteById(id);
    } else {
      throw new ObjectNotFoundException("DocAttribute with ID " + id + " not found.");
    }
  }
  @Override
  public List<DocAttribute> getDocAttributesByName(String name) {
    return docAttributeRepository.findByName(name);
  }
}
