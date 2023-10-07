package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.repositories.UserOrganizationRepository;
import ru.rosatom.documentflow.services.UserOrganizationService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserOrganizationServiceImpl implements UserOrganizationService {

    private final UserOrganizationRepository repository;

    @Override
    public UserOrganization getOrganization(Long orgId) {

        return repository.findById(orgId).orElseThrow(() -> new ObjectNotFoundException("There is no organization with this id"));
    }
}
