package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.models.OrgCreationRequest;
import ru.rosatom.documentflow.models.OrgUpdateRequest;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

public interface UserOrganizationService {

    UserOrganization getOrganization(Long orgId);

    Page<UserOrganization> getAllOrganizations(Pageable pageable);

    UserOrganization createOrganization(OrgCreationRequest orgCreationRequest);

    UserOrganization updateOrganization(Long orgId, OrgUpdateRequest orgUpdateRequest, User user);

    UserOrganization deleteOrganization(Long orgId);

    List<UserOrganization> getOrganizationsByNameLike(String name);

    List<UserOrganization> getAllActiveOrganization();

}
