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
import ru.rosatom.documentflow.repositories.UserOrganizationRepository;
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
    private final DocAttributeRepository docAttributeRepository;
    private final UserOrganizationRepository userOrganizationRepository;

    /**
     * Получает все типы документов с учетом пагинации. Если предоставлен идентификатор организации,
     * возвращает типы документов только для этой организации.
     *
     * @param pageable - параметры пагинации
     * @param orgId    - опциональный идентификатор организации
     * @return Page<DocType> - страница с типами документов
     */
    @Override
    public Page<DocType> getAllDocTypes(Pageable pageable, Optional<Long> orgId) {
        return orgId.map(id -> docTypeRepository.findAllByUserOrganization(id, pageable)).orElse(docTypeRepository.findAll(pageable));
    }

    /**
     * Получает тип документа по его идентификатору.
     *
     * @param id - идентификатор типа документа
     * @return DocType - найденный тип документа
     * @throws ObjectNotFoundException если тип документа не найден
     */
    @Override
    public DocType getDocTypeById(Long id) {
        return docTypeRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Тип документа с ID " + id + " не найден."));
    }

    /**
     * Создает новый тип документа на основе предоставленных данных.
     *
     * @param docTypeCreationRequest - запрос на создание типа документа, содержащий необходимую информацию
     * @return DocType - созданный тип документа
     */
    @Override
    public DocType createDocType(DocTypeCreationRequest docTypeCreationRequest) {
        UserOrganization userOrganization = userOrganizationRepository.findById(docTypeCreationRequest.getOrganizationId())
                .orElseThrow(() -> new ObjectNotFoundException("Организация с ID " + docTypeCreationRequest.getOrganizationId() + " не найдена."));

        List<DocAttribute> attributes = docTypeCreationRequest.getAttributes().stream()
                .map(attrId -> docAttributeRepository.findById(attrId)
                        .orElseThrow(() -> new ObjectNotFoundException("Атрибут с ID " + attrId + " не найден.")))
                .collect(Collectors.toList());

        DocType docType = DocType.builder()
                .name(docTypeCreationRequest.getName())
                .agreementType(docTypeCreationRequest.getAgreementType())
                .userOrganization(userOrganization)
                .attributes(attributes)
                .build();
        return docTypeRepository.save(docType);
    }

    /**
     * Обновляет существующий тип документа на основе предоставленных данных.
     *
     * @param docTypeId            - идентификатор обновляемого типа документа
     * @param docTypeUpdateRequest - запрос на обновление типа документа
     * @return DocType - обновленный тип документа
     */
    @Override
    public DocType updateDocType(Long docTypeId, DocTypeUpdateRequest docTypeUpdateRequest) {
        DocType docType = getDocTypeById(docTypeId);
        docType.setName(Objects.requireNonNullElse(docTypeUpdateRequest.getName(), docType.getName()));

        if (docTypeUpdateRequest.getAttributes() != null && !docTypeUpdateRequest.getAttributes().isEmpty()) {
            List<DocAttribute> updatedAttributes = docTypeUpdateRequest.getAttributes().stream()
                    .map(attrId -> docAttributeRepository.findById(attrId)
                            .orElseThrow(() -> new ObjectNotFoundException("Атрибут с ID " + attrId + " не найден.")))
                    .collect(Collectors.toList());
            docType.setAttributes(updatedAttributes);
        }
        return docTypeRepository.save(docType);
    }

    /**
     * Удаляет тип документа по его идентификатору.
     *
     * @param id - идентификатор удаляемого типа документа
     */
    @Override
    public void deleteDocType(Long id) {
        DocType docType = getDocTypeById(id);
        docTypeRepository.delete(docType);
    }

    /**
     * Поиск типа по подстроке в имени, при запросе от ADMIN поиск будет проходить по всей базе,
     * для остальных ролей поиск типов внутри своей компании.
     *
     * @param name - имя типа
     * @param user - пользователь отправивший запрос
     * @return DocType - список типов
     */
    @Override
    public List<DocType> getDocTypesByName(String name, User user) {
        List<DocType> docTypes;
        if (user.isAdmin()) {
            docTypes = docTypeRepository.findByNameContains(name);
        } else {
            docTypes = docTypeRepository.findByUserOrganizationIdAndNameContains(user.getOrganization().getId(), name);
        }
        return docTypes;
    }

    /**
     * Связывает атрибут с типом документа.
     *
     * @param docTypeId      - идентификатор типа документа
     * @param docAttributeId - идентификатор атрибута
     * @return DocType - обновленный тип документа с добавленным атрибутом
     */
    @Override
    public DocType attributeToType(Long docTypeId, Long docAttributeId) {
        DocType docType = docTypeRepository.findById(docTypeId)
                .orElseThrow(() -> new ObjectNotFoundException("Тип документа с ID " + docTypeId + " не найден."));
        DocAttribute docAttribute = docAttributeRepository.findById(docAttributeId)
                .orElseThrow(() -> new ObjectNotFoundException("Атрибут документа с ID " + docAttributeId + " не найден."));

        docType.addAttributes(docAttribute);

        return docTypeRepository.save(docType);
    }

    /**
     * Проверяет, разрешен ли доступ пользователю к определенному типу документа.
     *
     * @param id   - идентификатор типа документа
     * @param user - пользователь, для которого проверяется доступ
     * @return boolean - true, если доступ разрешен, иначе false
     */
    public boolean isAllowedType(Long id, User user) {
        return Objects.equals(getDocTypeById(id).getUserOrganization().getId(), user.getOrganization().getId());
    }

    /**
     * Проверяет, разрешен ли доступ пользователю к определенному атрибуту типа документа.
     *
     * @param docTypeId      - идентификатор типа документа
     * @param docAttributeId - идентификатор атрибута
     * @param user           - пользователь, для которого проверяется доступ
     * @return boolean - true, если доступ разрешен, иначе false
     */
    public boolean isAllowedTypeAttribute(Long docTypeId, Long docAttributeId, User user) {
        DocType docType = getDocTypeById(docTypeId);
        DocAttribute docAttribute = docAttributeRepository.findById(docAttributeId)
                .orElseThrow(() -> new ObjectNotFoundException("Атрибут документа с ID " + docAttributeId + " не найден."));

        return Objects.equals(docType.getUserOrganization().getId(), user.getOrganization().getId()) &&
                Objects.equals(docAttribute.getOrganization().getId(), user.getOrganization().getId());
    }
}
