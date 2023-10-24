package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.OrgCreationRequest;
import ru.rosatom.documentflow.models.OrgUpdateRequest;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

public interface  UserOrganizationService {

    UserOrganization getOrganization(Long orgId);

    List<UserOrganization> getAllOrganizations();

    UserOrganization createOrganization(OrgCreationRequest orgCreationRequest);

    UserOrganization updateOrganization(Long orgId, OrgUpdateRequest orgUpdateRequest);

    UserOrganization deleteOrganization(Long orgId);

    List<UserOrganization> getOrganizationsByNameLike(String name);

}
