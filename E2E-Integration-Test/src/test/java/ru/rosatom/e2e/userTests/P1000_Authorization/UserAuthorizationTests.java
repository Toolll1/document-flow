package ru.rosatom.e2e.userTests.P1000_Authorization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rosatom.e2e.BasicHttpTest;
import ru.rosatom.e2e.Environment;
import ru.rosatom.e2e.application.AppError;
import ru.rosatom.e2e.user.User;
import ru.rosatom.e2e.user.UserAuthorizationRequest;
import ru.rosatom.e2e.user.UserAuthorizationResponse;
import ru.rosatom.e2e.user.UserEndpoints;
import ru.rosatom.e2e.user.UserWithPassportAndOrg;

import java.util.stream.Stream;


@DisplayName("User authorization tests")
public class UserAuthorizationTests extends BasicHttpTest {

    UserAuthorizationResponse fedotovAuth;
    @DisplayName("Incorrect authorization: wrong password")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\"'@!:',m)(*&^"})
    public void testAuthWithWrongPassword(String password) {
        testAuthWithIncorrectParams(faker.internet().emailAddress(), password);
    }

    @DisplayName("Incorrect authorization: wrong email")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "hello", "@", "\"'@!:',m)(*&^"})
    public void testAuthWithWrongEmail(String email) {
        testAuthWithIncorrectParams(email, faker.internet().password());
    }


    @Test
    @DisplayName("Success authorization")
    @Order(1003)
    public void testSuccessLogin(){
        UserAuthorizationResponse userAuthorizationResponse = testSuccessAuth(new UserFedotovAuthRequest());
        setContextValue(Environment.USER_FEDOTOV_AUTHORIZATION, userAuthorizationResponse);
        fedotovAuth = userAuthorizationResponse;
    }


    @DisplayName("Success get profile info")
    @Test
    @Order(1004)
    public void testSuccessGetProfileInfo(){
        testSuccessLogin();
        sendGetAuthorizationInfo(fedotovAuth)
                .expectStatus().isOk()
                .expectBody(UserWithPassportAndOrg.class)
                .value(user -> {
                    assert user != null;
                    Stream.of(user.getId(), user.getFullName(),
                            user.getDateOfBirth(), user.getEmail(), user.getPhone(), user.getUserOrganization(),
                            user.getRole(), user.getPost(), user.getUserPassportDto()).forEach(Assertions::assertNotNull);
                });
    }





    private void testAuthWithIncorrectParams(String email, String password) {
        sendAuthRequest(new UserAuthorizationRequest(email, password))
                .expectStatus().isUnauthorized()
                .expectBody(AppError.class)
                .value(response -> {
                    assert response != null;
                    assert response.getMessage() != null;
                });
    }

    private UserAuthorizationResponse testSuccessAuth(UserAuthorizationRequest userAuthorizationRequest){
        return sendAuthRequest(userAuthorizationRequest)
                .expectStatus().isOk()
                .expectBody(UserAuthorizationResponse.class)
                .value(response -> {
                    assert response != null;
                    assert response.getToken() != null;
                    assert response.getUser() != null;
                    User user = response.getUser();
                    Stream.of(user.getId(), user.getLastName(), user.getFirstName(), user.getPatronymic(),
                            user.getDateOfBirth(), user.getEmail(), user.getPhone(), user.getOrganizationId(),
                            user.getRole(), user.getPost()).forEach(Assertions::assertNotNull);
                })
                .returnResult().getResponseBody();
    }



    private WebTestClient.ResponseSpec sendAuthRequest(UserAuthorizationRequest userAuthorizationRequest) {
        return withNotAuthClient().post().uri(UserEndpoints.AUTHORIZATION.getUrl())
                .bodyValue(userAuthorizationRequest)
                .exchange();
    }

    private WebTestClient.ResponseSpec sendGetAuthorizationInfo(UserAuthorizationResponse user){
        return withAuthClient(user)
                .get()
                .uri(UserEndpoints.AUTHORIZATION_INFO.getUrl())
                .exchange();

    }
}
