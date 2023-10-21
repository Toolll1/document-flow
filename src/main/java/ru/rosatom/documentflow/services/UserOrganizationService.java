package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

public interface UserOrganizationService {

    UserOrganization getOrganization(Long orgId);

    List<UserOrganization> getAllOrganizations();
}
