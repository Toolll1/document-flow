package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.AlreadyExistsException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.DocTypeService;
import ru.rosatom.documentflow.services.UserOrganizationService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeServiceImpl implements DocTypeService {

    private final DocTypeRepository docTypeRepository;
    private final DocAttributeService docAttributeService;
    private final UserOrganizationService userOrganizationService;

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
     * @param docTypeId - идентификатор типа документа
     * @return DocType - найденный тип документа
     * @throws ObjectNotFoundException если тип документа не найден
     */
    @Override
    public DocType getDocTypeById(Long docTypeId) {
        return docTypeRepository.findById(docTypeId)
                .orElseThrow(() -> new ObjectNotFoundException("Тип документа с ID " + docTypeId + " не найден."));
    }

    /**
     * Создает новый тип документа на основе предоставленных данных.
     *
     * @param docTypeCreationRequest - запрос на создание типа документа, содержащий необходимую информацию
     * @return DocType - созданный тип документа
     */
    @Override
    public DocType createDocType(DocTypeCreationRequest docTypeCreationRequest) {

        DocType docType = DocType.builder()
                .name(docTypeCreationRequest.getName())
                .agreementType(docTypeCreationRequest.getAgreementType())
                .organization(userOrganizationService.getOrganization(docTypeCreationRequest.getOrganizationId()))
                .attributes(docAttributeService.getAllByIdsElseThrow(docTypeCreationRequest.getAttributes()))
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

        if (docTypeUpdateRequest.getAttributes() != null) {
            docType.setAttributes(docAttributeService.getAllByIdsElseThrow(docTypeUpdateRequest.getAttributes()));
        }

        return docTypeRepository.save(docType);
    }

    /**
     * Удаляет тип документа по его идентификатору.
     *
     * @param docTypeId - идентификатор удаляемого типа документа
     */
    @Override
    public void deleteDocType(Long docTypeId) {
        DocType docType = docTypeRepository.findById(docTypeId)
                .orElseThrow(() -> new ObjectNotFoundException("Тип документа с ID " + docTypeId + " не найден."));

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
            docTypes = docTypeRepository.findByOrganizationIdAndNameContains(user.getOrganization().getId(), name);
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
        DocAttribute docAttribute = docAttributeService.getDocAttributeById(docAttributeId);
        if (docType.containsAttribute(docAttribute)){
            throw new AlreadyExistsException(docAttribute.getName(), docType.getName());
        }
        docType.addAttributes(docAttribute);
        return docTypeRepository.save(docType);
    }
    /**
     * Получает список всех типов документов, принадлежащих указанной организации.
     *
     * @param orgId Идентификатор организации, для которой необходимо получить типы.
     * @return List<DocType> Список типов документов, принадлежащих заданной организации.
     */
    @Override
    public List<DocType> findAllByOrganizationId(Long orgId) {
        return docTypeRepository.findAllByOrganizationId(userOrganizationService.getOrganization(orgId).getId()) ;
    }

    /**
     * Проверяет, разрешен ли доступ пользователю к определенному типу документа.
     *
     * @param id   - идентификатор типа документа
     * @param user - пользователь, для которого проверяется доступ
     * @return boolean - true, если доступ разрешен, иначе false
     */
    public boolean isAllowedType(Long id, User user) {
        return Objects.equals(getDocTypeById(id).getOrganization().getId(), user.getOrganization().getId());
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
        DocAttribute docAttribute = docAttributeService.getDocAttributeById(docAttributeId);

        return Objects.equals(docType.getOrganization().getId(), user.getOrganization().getId()) &&
                Objects.equals(docAttribute.getOrganization().getId(), user.getOrganization().getId());
    }
}
