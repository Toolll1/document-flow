package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocTypeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
  public List<DocType> getDocTypes(List<Long> ids, String sort, Integer from, Integer size) {
    PageRequest pageable = pageableCreator(from, size, sort);

    if (ids == null || ids.isEmpty()) {
      return docTypeRepository.findAll(pageable)
              .stream()
              .collect(Collectors.toList());
    } else {
      System.out.println(docTypeRepository.findAllByIdIn(ids, pageable));
      return docTypeRepository.findAllByIdIn(ids, pageable);
    }
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
      throw new ObjectNotFoundException("DocType with ID " + docTypeId + " not found.");
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

  @Override
  public List<DocType> getDocTypesByName(String name) {
    return docTypeRepository.findByNameContains(name);
  }

  @Override
  public DocType getDocTypeByName(String name) {
    return null;
  }

  private PageRequest pageableCreator(Integer from, Integer size, String sort) {

    if (sort == null || sort.isEmpty()) {
      return PageRequest.of(from / size, size);
    }

    switch (sort) {
      case "ID":
        return PageRequest.of(from / size, size, Sort.by("id"));
      case "LAST_NAME":
        return PageRequest.of(from / size, size, Sort.by("lastName"));
      default:
        throw new BadRequestException("Unknown sort: " + sort);
    }
  }
}
