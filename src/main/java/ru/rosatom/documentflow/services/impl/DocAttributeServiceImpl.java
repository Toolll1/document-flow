package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocAttributeServiceImpl implements DocAttributeService {

    private final DocAttributeRepository docAttributeRepository;

    @Override
    public Page<DocAttribute> getAllDocAttributes(Optional<Integer> page, Optional<String> sortBy) {
        return docAttributeRepository.findAll(
                PageRequest.of(page.orElse(0), 20, Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Override
    public DocAttribute getDocAttributeById(Long id) {
        Optional<DocAttribute> docAttribute = docAttributeRepository.findById(id);
        if (docAttribute.isPresent()) {
            return docAttribute.get();
        }
        throw new ObjectNotFoundException("DocAttribute c ID " + id + " не найден.");
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
    public DocAttribute updateDocAttribute(
            Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest) {
        if (docAttributeRepository.existsById(docAttributeId)) {
            DocAttribute docAttribute = getDocAttributeById(docAttributeId);
            docAttribute.setName(
                    Objects.requireNonNullElse(docAttributeUpdateRequest.getName(), docAttribute.getName()));
            docAttribute.setType(
                    Objects.requireNonNullElse(docAttributeUpdateRequest.getType(), docAttribute.getType()));
            return docAttributeRepository.save(docAttribute);
        } else {
            throw new ObjectNotFoundException("DocAttribute с ID " + docAttributeId + " не найден.");
        }
    }

    @Override
    public void deleteDocAttribute(Long id) {
        if (docAttributeRepository.existsById(id)) {
            docAttributeRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("DocAttribute с ID " + id + " не найден.");
        }
    }

    @Override
    public List<DocAttribute> getDocAttributesByName(String name) {
        return docAttributeRepository.findByNameContains(name);
    }


}
