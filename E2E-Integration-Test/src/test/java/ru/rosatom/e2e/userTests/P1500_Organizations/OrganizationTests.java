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

    UserAuthorizationResponse fedotovAuth = getContextValue(Environment.USER_FEDOTOV_AUTHORIZATION);

    @Test
    @DisplayName("Get all organizations")
    public void simpleGetAllOrganizations() {
        testGetAllOrganizationSuccess();
    }


    @Test
    @DisplayName("Get organizations with id")
    public void simpleGetOrganizationsWithId() {
        getOrganizationWithId(new OrganizationSearchRequestId(1));
    }

    @Test
    @DisplayName("Get organizations with incorrect id")
    public void simpleGetOrganizationsWithIncorrectId() {
        getOrganizationWithIncorrectId(new OrganizationSearchRequestId(-2));
    }

    @Test
    @DisplayName("Get organizations with name")
    public void simpleGetAllOrganizationsWithName() {
        getOrganizationWithName(new OrganizationSearchRequestName(" "));
    }

    @Test
    @DisplayName("Get organizations with incorrect name")
    public void simpleGetAllOrganizationsWithIncorrectName() {
        getOrganizationWithIncorrectName(new OrganizationSearchRequestName("."));
    }

    private WebTestClient.ResponseSpec getResponseSpecAllOrganizations() {
        return withAuthClient(fedotovAuth)
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
                        Assertions.assertNotNull(organization.getId());
                        Assertions.assertNotNull(organization.getName());
                        Assertions.assertNotNull(organization.getInn());
                    });
                });

    }

    private WebTestClient.ResponseSpec getResponseSpecOrganizationWithId(OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(fedotovAuth)
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
                }).returnResult().getResponseBody();
    }

    private void getOrganizationWithIncorrectId(OrganizationSearchRequestId organizationSearchRequestId) {
        getResponseSpecOrganizationWithId(organizationSearchRequestId)
                .expectStatus().is4xxClientError();
    }

    private WebTestClient.ResponseSpec getResponseSpecAllOrganizationsWithName(OrganizationSearchRequestName organizationSearchRequestName) {
        return withAuthClient(fedotovAuth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH_NAME + "/" + organizationSearchRequestName.getName())
                        .build())
                .exchange();
    }

    private void getOrganizationWithName(OrganizationSearchRequestName organizationSearchRequestName) {
        getResponseSpecAllOrganizationsWithName(organizationSearchRequestName)
                .expectStatus().isOk()
                .expectBodyList(OrganizationSearchResponseId.class) //OrganizationSearchResponse expectBodyList
                .value(response -> {
                    Assertions.assertNotNull(response);
                    for (OrganizationSearchResponseId organizationSearchResponseId : response) {
                        Stream.of(organizationSearchResponseId.getInn(), organizationSearchResponseId.getName()).forEach(Assertions::assertNotNull);
                    }
                }).returnResult().getResponseBody();
    }

    private void getOrganizationWithIncorrectName(OrganizationSearchRequestName organizationSearchRequestName) {
        getResponseSpecAllOrganizationsWithName(organizationSearchRequestName)
                .expectStatus().is5xxServerError();
    }
}
