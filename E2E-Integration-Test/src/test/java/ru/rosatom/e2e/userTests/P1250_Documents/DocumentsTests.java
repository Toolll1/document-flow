package ru.rosatom.e2e.userTests.P1250_Documents;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rosatom.e2e.BasicHttpTest;
import ru.rosatom.e2e.Environment;
import ru.rosatom.e2e.document.DocumentSearchResponse;
import ru.rosatom.e2e.document.DocumentSearchResponseId;
import ru.rosatom.e2e.document.DocumentsEndpoints;
import ru.rosatom.e2e.user.UserAuthorizationResponse;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

@DisplayName("Document tests")
public class DocumentsTests extends BasicHttpTest {
    UserAuthorizationResponse fedotovAuth = getContextValue(Environment.USER_FEDOTOV_AUTHORIZATION);

    @Test
    @DisplayName("Get all documents")
    public void simpleGetAllDocuments() {
        testGetAllDocumentSuccess();
    }

    private WebTestClient.ResponseSpec getResponseSpecAllDocuments() {
        return withAuthClient(fedotovAuth)
                .get()
                .uri(DocumentsEndpoints.DOCUMENT_SEARCH)
                .exchange();
    }

    private void testGetAllDocumentSuccess(){
        getResponseSpecAllDocuments()
                .expectStatus().isOk()
                .expectBody(DocumentSearchResponse.class)
                .value(documentSearchResponse -> {
                    documentSearchResponse.getDocuments().forEach(document -> {
                        Assertions.assertNotNull(document.getId());
                        Assertions.assertNotNull(document.getName());
                        Assertions.assertNotNull(document.getDocumentPath());
                        Assertions.assertNotNull(document.getDate());
                        Assertions.assertNotNull(document.getIdOrganization());
                        Assertions.assertNotNull(document.getOwnerId());
                        //Assertions.assertNotNull(document.getDocType());
                        Assertions.assertNotNull(document.getAttributeValues());
                        Assertions.assertNotNull(document.getFinalDocStatus());
                    });
                });
    }

    @Test
    @DisplayName("Get document with id")
    public void simpleGetDocumentWithId() {

        getDocumentWithId(new DocumentSearchRequestId(1));
    }

    private void getDocumentWithId(DocumentSearchRequestId documentSearchRequestId){
        getResponseSpecDocumentWithId(documentSearchRequestId)
                .expectStatus().isOk()
                .expectBody(DocumentSearchResponseId.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals(response.getId(), documentSearchRequestId.getId());
                    Stream.of(response.getDocumentPath(), response.getName(), response.getDate(),
                            response.getIdOrganization(), response.getOwnerId(),
                            //response.getDocType(),
                            response.getAttributeValues(),
                            response.getFinalDocStatus()).forEach(Assertions::assertNotNull);
                }).returnResult();
    }

    private WebTestClient.ResponseSpec getResponseSpecDocumentWithId(DocumentSearchRequestId documentSearchRequestId){
        return withAuthClient(fedotovAuth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(DocumentsEndpoints.DOCUMENT_SEARCH + "/" + documentSearchRequestId.getId())
                        .build())
                .exchange();
    }


}
