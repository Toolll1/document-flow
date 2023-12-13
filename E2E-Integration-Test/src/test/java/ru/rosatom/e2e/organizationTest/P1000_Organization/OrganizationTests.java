package ru.rosatom.e2e.organizationTest.P1000_Organization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rosatom.e2e.BasicHttpTest;
import ru.rosatom.e2e.Environment;
import ru.rosatom.e2e.organization.OrganizationSearchResponse;
import ru.rosatom.e2e.organization.OrganizationsEndpoint;

@DisplayName("Organization tests")
public class OrganizationTests extends BasicHttpTest {

    @Test
    @DisplayName("Get all organizations")
    public void simpleGetAllOrganizations() {
        testGetAllOrganizationSuccess();
    }

    @Test
    @DisplayName("Get all organizations with all param")
    public void simpleGetAllOrganizationsWithAllParam() {
        getOrganizationWithParam(new OrganizationSearchRequest(1, "Name ", "121212"));
    }

    @Test
    @DisplayName("Get organizations with id")
    public void simpleGetAllOrganizationsWithId() {
        getOrganizationWithId(new OrganizationSearchRequestId(1));
    }
    @Test
    @DisplayName("Get organizations with inn")
    public void simpleGetAllOrganizationsWithInn() {
        getOrganizationWithInn(new OrganizationSearchRequestInn("1232456"));
    }
    @Test
    @DisplayName("Get organizations with name")
    public void simpleGetAllOrganizationsWithName() {
        getOrganizationWithName(new OrganizationSearchRequestName("Росатом"));
    }

    private WebTestClient.ResponseSpec getAllOrganization() {
        return withAuthClient(Environment.ADMIN_AUTHORIZATION)
                .get()
                .uri(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                .exchange();
    }

    private WebTestClient.ResponseSpec getAllOrganizationWithAllParam(OrganizationSearchRequest organizationSearchRequest) {
        return withAuthClient(Environment.ADMIN_AUTHORIZATION)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                        .queryParam("id", organizationSearchRequest.getId())
                        .queryParam("name", organizationSearchRequest.getName())
                        .queryParam("inn", organizationSearchRequest.getInn())
                        .build())
                .exchange();
    }

    private void testGetAllOrganizationSuccess(){
        getAllOrganization().
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

    private void getOrganizationWithParam(OrganizationSearchRequest organizationSearchRequest){
        getAllOrganizationWithAllParam(organizationSearchRequest)
                .expectStatus().isOk()
                .expectBody(OrganizationSearchResponse.class)
                .value(organizationSearchResponse -> {
                    Assertions.assertNotNull(organizationSearchResponse.getOrganizations());
                    organizationSearchResponse.getOrganizations().forEach(attribute -> {
                        Assertions.assertNotNull(attribute.getId());
                        Assertions.assertNotNull(attribute.getName());
                        Assertions.assertNotNull(attribute.getInn());
                    });
                });
    }


    private WebTestClient.ResponseSpec getAllOrganizationWithId(OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(Environment.ADMIN_AUTHORIZATION)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                        .queryParam("id", organizationSearchRequestId.getId())
                        //.queryParam("name", organizationSearchRequest.getName())
                        //.queryParam("inn", organizationSearchRequest.getInn())
                        .build())
                .exchange();
    }

    private void getOrganizationWithId(OrganizationSearchRequestId organizationSearchRequestId){
        getAllOrganizationWithId(organizationSearchRequestId)
                .expectStatus().isOk()
                .expectBody(OrganizationSearchResponse.class)
                .value(organizationSearchResponse -> {
                    Assertions.assertNotNull(organizationSearchResponse.getOrganizations());
                    organizationSearchResponse.getOrganizations().forEach(attribute -> {
                        Assertions.assertNotNull(attribute.getId());
                        Assertions.assertNotNull(attribute.getName());
                        Assertions.assertNotNull(attribute.getInn());
                    });
                });
    }

    private WebTestClient.ResponseSpec getAllOrganizationWithInn(OrganizationSearchRequestInn organizationSearchRequestInn) {
        return withAuthClient(Environment.ADMIN_AUTHORIZATION)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                       // .queryParam("id", organizationSearchRequestId.getId())
                        //.queryParam("name", organizationSearchRequest.getName())
                        .queryParam("inn", organizationSearchRequestInn.getInn())
                        .build())
                .exchange();
    }

    private void getOrganizationWithInn(OrganizationSearchRequestInn organizationSearchRequestInn){
        getAllOrganizationWithInn(organizationSearchRequestInn)
                .expectStatus().isOk()
                .expectBody(OrganizationSearchResponse.class)
                .value(organizationSearchResponse -> {
                    Assertions.assertNotNull(organizationSearchResponse.getOrganizations());
                    organizationSearchResponse.getOrganizations().forEach(attribute -> {
                        Assertions.assertNotNull(attribute.getId());
                        Assertions.assertNotNull(attribute.getName());
                        Assertions.assertNotNull(attribute.getInn());
                    });
                });
    }

    private WebTestClient.ResponseSpec getAllOrganizationWithName(OrganizationSearchRequestName organizationSearchRequestName) {
        return withAuthClient(Environment.ADMIN_AUTHORIZATION)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                        .queryParam("name", organizationSearchRequestName.getName())
                        .build())
                .exchange();
    }

    private void getOrganizationWithName(OrganizationSearchRequestName organizationSearchRequestName){
        getAllOrganizationWithName(organizationSearchRequestName)
                .expectStatus().isOk()
                .expectBody(OrganizationSearchResponse.class)
                .value(organizationSearchResponse -> {
                    Assertions.assertNotNull(organizationSearchResponse.getOrganizations());
                    organizationSearchResponse.getOrganizations().forEach(attribute -> {
                        Assertions.assertNotNull(attribute.getId());
                        Assertions.assertNotNull(attribute.getName());
                        Assertions.assertNotNull(attribute.getInn());
                    });
                });
    }

}
