package ru.rosatom.e2e.userTests.P1500_Organizations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rosatom.e2e.BasicHttpTest;
import ru.rosatom.e2e.Environment;
import ru.rosatom.e2e.organization.*;
import ru.rosatom.e2e.user.UserAuthorizationResponse;

import java.util.stream.Stream;

@DisplayName("Organization tests")
public class OrganizationTests extends BasicHttpTest {

    UserAuthorizationResponse userOrg1Auth = getContextValue(Environment.USER_FEDOTOV_AUTHORIZATION);
    UserAuthorizationResponse userOrg2Auth = getContextValue(Environment.USER_ANTONOV_AUTHORIZATION);
    UserAuthorizationResponse adminOrg2Auth = getContextValue(Environment.USER_ANDREEV_AUTHORIZATION);

    @Test
    @DisplayName("Get all organizations")
    public void getAllOrganizations() {
        testGetAllOrganizationSuccess();
    }


    @Test
    @DisplayName("Get organizations with id")
    public void getOrganizationsWithId() {
        getOrganizationWithId(new OrganizationSearchRequestId(1));
    }

    @Test
    @DisplayName("Get organizations with incorrect id")
    public void getOrganizationsWithIncorrectId() {
        getOrganizationWithIncorrectId(new OrganizationSearchRequestId(-2));
    }

    @Test
    @DisplayName("Get organizations with name")
    public void getAllOrganizationsWithName() {
        getOrganizationWithName(new OrganizationSearchRequestName(" "));
    }

    @Test
    @DisplayName("Get organizations with preffix")
    public void getOrganizationWithNamePreffix() {
        getOrganizationWithNamePreffix(new OrganizationSearchRequestName("Рос"));
    }

    @Test
    @DisplayName("Add organization - fail forbidden")
    public void addOrganizationForbidden() {
        addOrganizationForbidden(new OrganizationAddRequest("New organization", "1234567890"), adminOrg2Auth);
    }


    @Test
    @DisplayName("Update organization")
    public void updateOrganization() {
        updateOrganization(new OrganizationUpdateRequest(faker.company().name()), new OrganizationSearchRequestId(2));
    }

    @Test
    @DisplayName("Update organization Fail")
    public void updateOrganizationFail() {
        updateOrganizationFail(new OrganizationUpdateRequest(faker.company().name()), new OrganizationSearchRequestId(1));
    }


    @Test
    @DisplayName("Delete organization Fail")
    public void deleteOrganizationFail() {
        deleteOrganizationFail( new OrganizationSearchRequestId(2), userOrg1Auth);

    }


    private WebTestClient.ResponseSpec getResponseSpecAllOrganizations() {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                .exchange();
    }

    private void testGetAllOrganizationSuccess() {
        getResponseSpecAllOrganizations().
                expectStatus().isOk()
                .expectBody(OrganizationSearchResponse.class).
                value(organizationSearchResponse -> {
                    organizationSearchResponse.getOrganizations().forEach(organization -> {
                        Assertions.assertNotNull(organization);
                        Assertions.assertNotNull(organization.getId());
                        Assertions.assertNotNull(organization.getName());
                        Assertions.assertNotNull(organization.getInn());
                    });
                });

    }

    private WebTestClient.ResponseSpec getResponseSpecOrganizationWithId(OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .exchange();
    }

    private void getOrganizationWithId(OrganizationSearchRequestId organizationSearchRequestId) {
        getResponseSpecOrganizationWithId(organizationSearchRequestId)
                .expectStatus().isOk()
                .expectBody(OrganizationSearchResponseId.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals(response.getId(), organizationSearchRequestId.getId());
                    Stream.of(response.getInn(), response.getName()).forEach(Assertions::assertNotNull);
                }).returnResult();
    }

    private void getOrganizationWithIncorrectId(OrganizationSearchRequestId organizationSearchRequestId) {
        getResponseSpecOrganizationWithId(organizationSearchRequestId)
                .expectStatus().isNotFound()
                .expectBody(OrganizationSearchResponseId.class)
                .value(Assertions::assertNotNull).returnResult();
    }

    private WebTestClient.ResponseSpec getResponseSpecAllOrganizationsWithName(OrganizationSearchRequestName organizationSearchRequestName) {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH_NAME+ organizationSearchRequestName.getName())
                        .build())
                .exchange();
    }

    private void getOrganizationWithName(OrganizationSearchRequestName organizationSearchRequestName) {
        getResponseSpecAllOrganizationsWithName(organizationSearchRequestName)
                .expectStatus().isOk()
                .expectBodyList(OrganizationSearchResponseId.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    for (OrganizationSearchResponseId organizationSearchResponseId : response) {
                        Stream.of(organizationSearchResponseId.getInn(), organizationSearchResponseId.getName()).forEach(Assertions::assertNotNull);
                    }
                }).returnResult();
    }

    private void getOrganizationWithNamePreffix(OrganizationSearchRequestName organizationSearchRequestName) {
        OrganizationSearchResponseId organization1 = new OrganizationSearchResponseId(5,"Ростелеком", "9938230050");
        OrganizationSearchResponseId organization2 = new OrganizationSearchResponseId(6,"Ростагрокомплекс", "5995681370");
        getResponseSpecAllOrganizationsWithName(organizationSearchRequestName)
                .expectStatus().isOk()
                .expectBodyList(OrganizationSearchResponseId.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    for (OrganizationSearchResponseId organizationSearchResponseId : response) {
                        Stream.of(organizationSearchResponseId.getInn(), organizationSearchResponseId.getName()).forEach(Assertions::assertNotNull);
                    }
                    Assertions.assertTrue(response.contains(organization1));
                    Assertions.assertTrue(response.contains(organization2));
                }).returnResult();
    }

    private void deleteOrganizationFail(OrganizationSearchRequestId organizationSearchRequestId,UserAuthorizationResponse userAuthorizationResponse){
        getResponseSpecDeleteOrganizationFail(organizationSearchRequestId, userAuthorizationResponse)
                .expectStatus().isForbidden().expectBody(OrganizationSearchResponseId.class)
                .value(Assertions::assertNotNull)
                .returnResult();
    }


    private void updateOrganization(OrganizationUpdateRequest organizationUpdateRequest,
                                    OrganizationSearchRequestId organizationSearchRequestId){
        getResponseSpecUpdateOrganization(organizationUpdateRequest, organizationSearchRequestId)
                .expectStatus().isOk()
                .expectBody(OrganizationUpdateResponse.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Stream.of(response.getId(), response.getInn(), response.getName()).forEach(Assertions::assertNotNull);
                    Assertions.assertEquals(response.getName(), organizationUpdateRequest.getName());
                     Assertions.assertEquals(response.getId(), organizationSearchRequestId.getId());
                }).returnResult();
    }

    private void updateOrganizationFail(OrganizationUpdateRequest organizationUpdateRequest,
                                        OrganizationSearchRequestId organizationSearchRequestId) {
        getResponseSpecUpdateOrganizationFail(organizationUpdateRequest, organizationSearchRequestId)
                .expectStatus().isForbidden()
                .expectBody(OrganizationUpdateResponse.class)
                .value(Assertions::assertNotNull)
                .returnResult();
    }

    private WebTestClient.ResponseSpec getResponseSpecUpdateOrganizationFail(OrganizationUpdateRequest organizationUpdateRequest,
                                                                         OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(userOrg2Auth)
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .bodyValue(organizationUpdateRequest)
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponseSpecUpdateOrganization(OrganizationUpdateRequest organizationUpdateRequest,
                                                                         OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(adminOrg2Auth)
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .bodyValue(organizationUpdateRequest)
                .exchange();
    }


    private WebTestClient.ResponseSpec getResponseSpecDeleteOrganizationFail(OrganizationSearchRequestId organizationSearchRequestId,
                                                                             UserAuthorizationResponse userAuthorizationResponse) {
        return withAuthClient(userAuthorizationResponse)
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponseSpecAddOrganizationForbidden(OrganizationAddRequest organizationAddRequest,
                                                                               UserAuthorizationResponse userAuthorizationResponse){
        return withAuthClient(userAuthorizationResponse)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                        .build())
                .bodyValue(organizationAddRequest)
                .exchange();
    }

    private void addOrganizationForbidden(OrganizationAddRequest organizationAddRequest,  UserAuthorizationResponse userAuthorizationResponse){
        getResponseSpecAddOrganizationForbidden(organizationAddRequest, userAuthorizationResponse )
                .expectStatus().isForbidden().expectBody(OrganizationAddResponce.class)
                .value(Assertions::assertNotNull)
                .returnResult();;
    }
}