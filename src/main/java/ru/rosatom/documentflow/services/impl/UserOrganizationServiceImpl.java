package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.adapters.CommonUtils;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.OrgCreationRequest;
import ru.rosatom.documentflow.models.OrgUpdateRequest;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.repositories.UserOrganizationRepository;
import ru.rosatom.documentflow.services.UserOrganizationService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserOrganizationServiceImpl implements UserOrganizationService {

    private final UserOrganizationRepository repository;

    private final CommonUtils commonUtils;

    @Override
    public UserOrganization getOrganization(Long orgId) {
        return repository
                .findById(orgId).
                orElseThrow(() -> new ObjectNotFoundException("There is no organization with this id"));
    }


    /**
     * Получить все организации
     *
     * @return List<UserOrganization> список организаций
     */
    @Override
    public Page<UserOrganization> getAllOrganizations(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Создать организацию
     *
     * @param orgCreationRequest запрос на создание организации
     * @return UserOrganization созданная организация
     */
    @Override
    public UserOrganization createOrganization(OrgCreationRequest orgCreationRequest) {
        throwIfOrganizationExists(orgCreationRequest.getName());
        UserOrganization organization = UserOrganization.builder()
                .name(orgCreationRequest.getName())
                .inn(orgCreationRequest.getInn())
                .build();
        return repository.save(organization);
    }

    /**
     * Обновить организацию при запросе от ADMIN по указанной компании,
     * для остальных ролей всегда обновиться своя компания.
     *
     * @param orgId            id организации
     * @param orgUpdateRequest запрос на обновление организации
     * @param user
     * @return UserOrganization обновленная организация
     */
    @Override
    public UserOrganization updateOrganization(Long orgId, OrgUpdateRequest orgUpdateRequest, User user) {
        if (!commonUtils.isAdmin(user)){
            orgId = user.getOrganization().getId();
        }
        throwIfOrganizationExists(orgId, orgUpdateRequest.getName());
        UserOrganization organization = getOrganization(orgId);
        organization.setName(Objects.requireNonNullElse(orgUpdateRequest.getName(), organization.getName()));
        return repository.save(organization);
    }

    /**
     * Удалить организацию
     *
     * @param orgId id организации
     * @return UserOrganization удаленная организация
     */
    @Override
    public UserOrganization deleteOrganization(Long orgId) {
        UserOrganization organization = getOrganization(orgId);
        try {
            repository.delete(organization);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cannot delete organization " +
                    "with id " + orgId + " because users are associated with it. Please delete users or change their organization first.");
        }
        return organization;
    }

    /**
     * Получить организацию по имени
     *
     * @param name подстрока имени организации
     * @return UserOrganization организация
     */
    @Override
    public List<UserOrganization> getOrganizationsByNameLike(String name) {
        return repository.findByNameContainsIgnoreCase(name);
    }

    @Override
    public List<UserOrganization> getAllActiveOrganization() {
        return repository.findActiveOrganization();
    }


    private void throwIfOrganizationExists(Long ignoreId, String name) {
        Optional<UserOrganization> organization = repository.findByName(name);
        if (organization.isPresent() && !organization.get().getId().equals(ignoreId)) {
            throw new ConflictException("Organization with this name already exists");
        }
    }

    private void throwIfOrganizationExists(String name) {
        throwIfOrganizationExists(-1L, name);
    }


}
