package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.UserOrganizationService;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DocAttributeServiceImpl implements DocAttributeService {

    private final DocAttributeRepository docAttributeRepository;
    private final UserOrganizationService userOrganizationService;

    /**
     * Получение всех атрибутов документов с учетом пагинации.
     * Если предоставлен идентификатор организации, возвращает атрибуты только для этой организации.
     *
     * @param pageable Параметры пагинации
     * @param orgId    Опциональный идентификатор организации
     * @return Page<DocAttribute> Страница с атрибутами документов
     */
    @Override
    public Page<DocAttribute> getAllDocAttributes(Pageable pageable, Optional<Long> orgId) {
        return orgId.map(id -> docAttributeRepository.findAllByUserOrganization(id, pageable))
                .orElse(docAttributeRepository.findAll(pageable));
    }

    /**
     * Получение атрибута документа по его идентификатору.
     *
     * @param id Идентификатор атрибута документа
     * @return DocAttribute Найденный атрибут документа
     * @throws ObjectNotFoundException Если атрибут не найден
     */
    @Override
    public DocAttribute getDocAttributeById(Long id) {
        return docAttributeRepository
                .findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Атрибут документа c ID " + id + " не найден."));
    }

    /**
     * Создание нового атрибута документа.
     *
     * @param docAttributeCreationRequest Запрос на создание атрибута документа
     * @return DocAttribute Созданный атрибут документа
     */
    @Override
    public DocAttribute createDocAttribute(DocAttributeCreationRequest docAttributeCreationRequest) {
        DocAttribute docAttribute =
                DocAttribute.builder()
                        .name(docAttributeCreationRequest.getName())
                        .type(docAttributeCreationRequest.getType())
                        .organization(userOrganizationService.getOrganization(docAttributeCreationRequest.getOrganizationId()))
                        .build();
        return docAttributeRepository.save(docAttribute);
    }

    /**
     * Обновление существующего атрибута документа.
     *
     * @param docAttributeId            Идентификатор обновляемого атрибута
     * @param docAttributeUpdateRequest Запрос на обновление атрибута документа
     * @return DocAttribute Обновленный атрибут документа
     */
    @Override
    public DocAttribute updateDocAttribute(
            Long docAttributeId, DocAttributeUpdateRequest docAttributeUpdateRequest) {
        DocAttribute docAttribute = getDocAttributeById(docAttributeId);
        docAttribute.setName(
                Objects.requireNonNullElse(docAttributeUpdateRequest.getName(), docAttribute.getName()));
        docAttribute.setType(
                Objects.requireNonNullElse(docAttributeUpdateRequest.getType(), docAttribute.getType()));
        return docAttributeRepository.save(docAttribute);
    }

    /**
     * Удаление атрибута документа по его идентификатору.
     *
     * @param id Идентификатор удаляемого атрибута
     */
    @Override
    public void deleteDocAttribute(Long id) {
        docAttributeRepository.delete(getDocAttributeById(id));
    }

    /**
     * Поиск атрибута по подстроке в имени, при запросе от ADMIN поиск будет проходить по всей базе,
     * для остальных ролей поиск атрибутов внутри своей компании.
     *
     * @param name - имя аттрибута
     * @param user - пользователь отправивший запрос
     * @return DocAttribute - список аттрибутов
     */
    @Override
    public List<DocAttribute> getDocAttributesByName(String name, User user) {
        List<DocAttribute> docAttributes;
        if (user.isAdmin()) {
            docAttributes = docAttributeRepository.findByNameContains(name);
        } else {
            docAttributes = docAttributeRepository.findByOrganizationIdAndNameContains(user.getOrganization().getId(), name);
        }
        return docAttributes;
    }

    /**
     * Проверяет, разрешен ли доступ к атрибуту для заданного пользователя.
     * Доступ разрешен, если атрибут принадлежит организации пользователя.
     *
     * @param id   Идентификатор атрибута
     * @param user Пользователь, для которого проверяется доступ
     * @return boolean true, если доступ разрешен, иначе false
     */
    public boolean isAllowedAttribute(Long id, User user) {
        return Objects.equals(getDocAttributeById(id).getOrganization().getId(), user.getOrganization().getId());
    }

    /**
     * Получает список атрибутов документа по их идентификаторам.
     * Этот метод использует {@link DocAttributeRepository#findAllById(Iterable)} для извлечения атрибутов,
     * что позволяет оптимизировать производительность за счет уменьшения количества запросов к базе данных.
     * Вместо множественных запросов для каждого идентификатора, все атрибуты извлекаются за один запрос.
     *
     * @param ids Список идентификаторов атрибутов документа, которые необходимо получить.
     * @return Список {@link DocAttribute}, соответствующих предоставленным идентификаторам.
     *         Если некоторые идентификаторы не найдены, они будут просто пропущены в возвращаемом списке.
     */
    @Override
    public List<DocAttribute> getDocAttributesByIds(List<Long> ids) {
        return docAttributeRepository.findAllById(ids);
    }
}
