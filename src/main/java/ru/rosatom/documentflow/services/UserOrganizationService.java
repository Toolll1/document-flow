package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.UserOrganization;

public interface UserOrganizationService {

    UserOrganization getOrganization(Long orgId);
}
