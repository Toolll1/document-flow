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

    UserAuthorizationResponse fedotovAuth = getContextValue(Environment.USER_FEDOTOV_AUTHORIZATION); //орг 1
    UserAuthorizationResponse antonovAuth = getContextValue(Environment.USER_ANTONOV_AUTHORIZATION); // орг 2
    UserAuthorizationResponse andreevAuth = getContextValue(Environment.USER_ANDREEV_AUTHORIZATION); //админ орг 2

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


    @Test
    @DisplayName("Add organization - fail forbidden")
    public void simpleAddOrganizationFordidden() {
        addOrganizationForbidden(new OrganizationAddRequest("New organization", "1234567890"));
    }


    @Test
    @DisplayName("Update organization")
    public void simpleUpdateOrganization() {
        updateOrganization(new OrganizationUpdateRequest("New name"), new OrganizationSearchRequestId(2));
    }

    @Test
    @DisplayName("Update organization Fail")
    public void simpleUpdateOrganizationFail() {
        updateOrganizationFail(new OrganizationUpdateRequest("New name"), new OrganizationSearchRequestId(1));
    }

    @Test
    @DisplayName("Delete organization")
    public void simpleDeleteOrganization() {
        deleteOrganization( new OrganizationSearchRequestId(2));

    }

    @Test
    @DisplayName("Delete organization Fail")
    public void simpleDeleteOrganizationFail() {
        deleteOrganizationFail( new OrganizationSearchRequestId(2));

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
                }).returnResult().getResponseBody();
    }

    private void getOrganizationWithIncorrectName(OrganizationSearchRequestName organizationSearchRequestName) {
        getResponseSpecAllOrganizationsWithName(organizationSearchRequestName)
                .expectStatus().is5xxServerError();
    }


    private void deleteOrganization(OrganizationSearchRequestId organizationSearchRequestId){
        getResponseSpecDeleteOrganization(organizationSearchRequestId)
                .expectStatus().isOk();
    }

    private void deleteOrganizationFail(OrganizationSearchRequestId organizationSearchRequestId){
        getResponseSpecDeleteOrganizationFail(organizationSearchRequestId)
                .expectStatus().isForbidden();
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
                }).returnResult().getResponseBody();
    }

    private void updateOrganizationFail(OrganizationUpdateRequest organizationUpdateRequest,
                                        OrganizationSearchRequestId organizationSearchRequestId) {
        getResponseSpecUpdateOrganizationFail(organizationUpdateRequest, organizationSearchRequestId)
                .expectStatus().is5xxServerError();
    }

    private WebTestClient.ResponseSpec getResponseSpecUpdateOrganizationFail(OrganizationUpdateRequest organizationUpdateRequest,
                                                                         OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(antonovAuth) // тут  юзер другой орг
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .bodyValue(organizationUpdateRequest)
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponseSpecUpdateOrganization(OrganizationUpdateRequest organizationUpdateRequest,
                                                                         OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(andreevAuth)
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId()) //  + organizationUpdateRequest.getId())
                        .build())
                .bodyValue(organizationUpdateRequest)
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponseSpecDeleteOrganization(OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(andreevAuth) // админ 2 компании
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponseSpecDeleteOrganizationFail(OrganizationSearchRequestId organizationSearchRequestId) {
        return withAuthClient(fedotovAuth) // юзер - ошибка
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH + "/" + organizationSearchRequestId.getId())
                        .build())
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponseSpecAddOrganizationForbidden(OrganizationAddRequest organizationAddRequest) {
        return withAuthClient(fedotovAuth)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)
                        .build())
                .bodyValue(organizationAddRequest)
                .exchange();
    }

    private void addOrganizationForbidden(OrganizationAddRequest organizationAddRequest){
        getResponseSpecAddOrganizationForbidden(organizationAddRequest)
                .expectStatus().isForbidden();
    }


  // МЕТОД ДЛЯ АДМИНА:
//    @Test
//    @DisplayName("Add organization")
//    public void simpleAddOrganization() {
//        addOrganization(new OrganizationAddRequest("New organization", "1234567890"));
//    }

//    private void addOrganization(OrganizationAddRequest organizationAddRequest) {
//        getResponseSpecAddOrganization(organizationAddRequest)
//                .expectStatus().isOk()
//                .expectBody(OrganizationAddResponce.class)
//                .value(response -> {
//                    Assertions.assertNotNull(response);
//                    Stream.of(response.getInn(), response.getName()).forEach(Assertions::assertNotNull);
//                    Assertions.assertEquals(response.getName(), organizationAddRequest.getName());
//                    Assertions.assertEquals(response.getInn(), organizationAddRequest.getInn());
//                }).returnResult().getResponseBody();
//    }
//private WebTestClient.ResponseSpec getResponseSpecAddOrganization(OrganizationAddRequest organizationAddRequest) {
//    return withAuthClient(fedotovAuth)  // тут поменять на админа
//            .post()
//            .uri(uriBuilder -> uriBuilder
//                    .path(OrganizationsEndpoint.ORGANIZATION_SEARCH)  //+ "/" + organizationAddRequest.getId())
//                    .build())
//            .bodyValue(organizationAddRequest)
//            .exchange();
//}
}
