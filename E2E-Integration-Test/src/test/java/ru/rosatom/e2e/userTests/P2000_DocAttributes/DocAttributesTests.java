package ru.rosatom.e2e.userTests.P2000_DocAttributes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rosatom.e2e.BasicHttpTest;
import ru.rosatom.e2e.Environment;
import ru.rosatom.e2e.attribute.AttributeSearchResponse;
import ru.rosatom.e2e.attribute.AttributesEndpoints;
import ru.rosatom.e2e.user.UserAuthorizationResponse;

import java.util.List;
import java.util.StringJoiner;

@DisplayName("Document attributes tests")
public class DocAttributesTests extends BasicHttpTest {

    UserAuthorizationResponse fedotovAuth = getContextValue(Environment.USER_FEDOTOV_AUTHORIZATION);

    @Test
    @DisplayName("Get all attributes with default params")
    public void simpleGetAllAttributes() {
        testGetAllAttributesSuccess(new AttributeSearchRequest(0, 10, List.of("id", "ASC"), 1));
    }

    private WebTestClient.ResponseSpec getAllAttributes(AttributeSearchRequest attributeSearchRequest) {
        StringJoiner sortjoiner = new StringJoiner(",");
        attributeSearchRequest.getSort().forEach(sortjoiner::add);
        return withAuthClient(Environment.USER_FEDOTOV_AUTHORIZATION)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(AttributesEndpoints.ATTRIBUTE_SEARCH)
                        .queryParam("page", attributeSearchRequest.getPage())
                        .queryParam("size", attributeSearchRequest.getSize())
                        .queryParam("sort", sortjoiner.toString())
                        .queryParam("org_id", attributeSearchRequest.getOrgId())
                        .build())
                .exchange();
    }

    private void testGetAllAttributesSuccess(AttributeSearchRequest attributeSearchRequest){
        getAllAttributes(attributeSearchRequest)
                .expectStatus().isOk()
                .expectBody(AttributeSearchResponse.class)
                .value(attributeSearchResponse -> {
                    Assertions.assertNotNull(attributeSearchResponse.getAttributes());
                    Assertions.assertTrue(attributeSearchResponse.getAttributes().size() <= attributeSearchRequest.getSize());
                    attributeSearchResponse.getAttributes().forEach(attribute -> {
                        Assertions.assertNotNull(attribute.getId());
                        Assertions.assertNotNull(attribute.getName());
                        Assertions.assertNotNull(attribute.getOrganization());
                        Assertions.assertNotNull(attribute.getType());
                    });
                });
    }
}
