package ru.rosatom.documentflow.services.impl;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.UserOrganizationRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserOrganizationServiceImplTest {
    @Autowired
    UserOrganizationServiceImpl userOrganizationService;
    @MockBean
    private UserOrganizationRepository repository;
    private UserOrganization userOrganization;

    @BeforeEach
    void setUp() {
        userOrganization = new UserOrganization(1L, "12121212", "Name");
    }

    @Test
    void getOrganization() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(userOrganization));
        Assert.assertEquals(Optional.of(userOrganization), repository.findById(1L));
    }

    @Test
    void getOrganizationFalse() {
        Assert.assertFalse(repository.findById(9L).isPresent());
        Mockito.when(repository.findById(9L)).thenThrow(ObjectNotFoundException.class);
    }

    @Test
    void getAllOrganizations() {
        userOrganizationService.getAllOrganizations();
        Mockito.verify(repository).findAll();
        List<UserOrganization> organisations = new ArrayList<>(Arrays.asList(new UserOrganization(), new UserOrganization(), new UserOrganization()));
        Mockito.when(repository.findAll()).thenReturn(organisations);
    }

    @Test
    void createOrganization() {
        OrgCreationRequest orgCreationRequest = new OrgCreationRequest();
        orgCreationRequest.setInn("333333");
        orgCreationRequest.setName("Name");
        Mockito.when(userOrganizationService.createOrganization(orgCreationRequest)).thenReturn(new UserOrganization());
    }

    @Test
    void updateOrganization() {
        OrgUpdateRequest orgUpdateRequest = new OrgUpdateRequest();
        orgUpdateRequest.setName("New Name");
        UserOrganization updateOrganisation = new UserOrganization(userOrganization.getId(), userOrganization.getInn(), orgUpdateRequest.getName());
        Mockito.when(repository.findById(userOrganization.getId())).thenReturn(Optional.of(userOrganization));
        Mockito.when(repository.save(updateOrganisation)).thenReturn(updateOrganisation);
        Assert.assertEquals(updateOrganisation.getName(), orgUpdateRequest.getName());
    }

    @Test
    void deleteOrganization() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(userOrganization)).thenReturn(null);
    }

    @Test
    void getOrganizationsByNameLike() {
        Mockito.when(userOrganizationService.getOrganizationsByNameLike("Name")).thenReturn(List.of(userOrganization));
    }

    @Test
    void getAllActiveOrganization() {
        Document document = new Document();
        document.setIdOrganization(1L);
        List<UserOrganization> organisations = new ArrayList<>(Arrays.asList(new UserOrganization(2L , "1111", "Name2"), new UserOrganization(3L , "333", "Name3"), new UserOrganization(4L , "444", "Name4")));
        List<UserOrganization> organisationActive = new ArrayList<>();
        for (UserOrganization organization : organisations) {
            if (organization.getId().equals(document.getIdOrganization())) {
                {
                    organisationActive.add(organization);
                }
            }
        }
        Mockito.when(repository.findActiveOrganization()).thenReturn(organisationActive);
    }
}