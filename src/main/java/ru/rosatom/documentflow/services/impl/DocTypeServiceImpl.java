package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.adapters.TranslitText;
import ru.rosatom.documentflow.exceptions.AlreadyExistsException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocTypeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.DocTypeService;
import ru.rosatom.documentflow.services.UserOrganizationService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        String checkResult = checkName(docTypeCreationRequest.getName());
        if (!checkResult.isEmpty()) {
            throw new ConflictException(checkResult);
        }

        DocType docType = DocType.builder()
                .name(docTypeCreationRequest.getName())
                .agreementType(docTypeCreationRequest.getAgreementType())
                .organization(userOrganizationService.getOrganization(docTypeCreationRequest.getOrganizationId()))
                .attributes(docAttributeService.getAllByIdsElseThrow(docTypeCreationRequest.getAttributes()))
                .build();
        return docTypeRepository.save(docType);
    }

    /**
     * Проверяет корректность имени типа документов на соответствие требованиям minio
     *
     * @param name - имя типа документа
     * @return String - список ошибок в имени
     */
    private String checkName(String name) {

        String input = TranslitText.transliterate(name);
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9.-]");
        Matcher matcher = pattern.matcher(input);
        Set<String> incorrectSymbols = new HashSet<>();

        while (matcher.find()) {
            incorrectSymbols.add(matcher.group());
        }
        if (!incorrectSymbols.isEmpty()) {
            sb.append("В имени типа документа присутствуют недопустимые символы: ").append(incorrectSymbols).append("\n");
        }
        if (input.length() < 3 || input.length() > 63) {
            sb.append("Длина имени типа документа без учета пробелов должна быть от 3 до 63 символов\n");
        }
        if (!input.matches("^[a-zA-Z0-9].*[a-zA-Z0-9]$")) {
            sb.append("Имя типа документы должно начинаться и заканчиваться буквой или цифрой\n");
        }
        if (input.contains("..")) {
            sb.append("Имя типа документы не должно содержать двух последовательных точек\n");
        }
        if (input.matches("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")) {
            sb.append("Имя типа документы не должно быть отформатировано, как IP-адрес\n");
        }
        if (!input.matches("^(?!xn--|sthree-|sthree-configurator)(?!.*-s3alias$|.*--ol-s3$).*$")) {
            sb.append("Имя типа документы не должно начинаться на \"xn--\", \"sthree-\" и  \"sthree-configurator\"\n" +
                    "и не должно заканчиваться на \"-s3alias\" и \"--ol-s3\"");
        }

        return sb.toString();
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
        if (docType.containsAttribute(docAttribute)) {
            throw new AlreadyExistsException(docAttribute.getName(), docType.getName());
        }
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
