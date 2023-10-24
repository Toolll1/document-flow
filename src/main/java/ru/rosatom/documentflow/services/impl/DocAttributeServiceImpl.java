package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocAttributeServiceImpl implements DocAttributeService {

    private final DocAttributeRepository docAttributeRepository;

    @Override
    public Page<DocAttribute> getAllDocAttributes(Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return docAttributeRepository.findAll(pageable);
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
    public DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest) {
        DocAttribute docAttribute =
                DocAttribute.builder()
                        .name(docAttributeCreationRequest.getName())
                        .type(docAttributeCreationRequest.getType())
                        .build();
        return docAttributeRepository.save(docAttribute);
    }

    @Override
    public DocAttribute updateDocAttribute(DocAttribute docAttribute) {
        if (docAttributeRepository.existsById(docAttribute.getId())) {
            return docAttributeRepository.save(docAttribute);
        } else {
            throw new ObjectNotFoundException(
                    "DocAttribute with ID " + docAttribute.getId() + " not found.");
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

    private Pageable createPageable(Integer page, Integer size, String sort) {
        Sort sorted = Sort.unsorted();

        if (sort != null) {
            if (sort.equals("name")) {
                sorted = Sort.by("name");
            } else if (sort.equals("type")) {
                sorted = Sort.by("type");
            }
        }

        return PageRequest.of(page, size, sorted);
    }
}
