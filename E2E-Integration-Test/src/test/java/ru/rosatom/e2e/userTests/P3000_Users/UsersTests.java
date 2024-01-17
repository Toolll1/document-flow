package ru.rosatom.e2e.userTests.P3000_Users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rosatom.e2e.BasicHttpTest;
import ru.rosatom.e2e.Environment;
import ru.rosatom.e2e.user.User;
import ru.rosatom.e2e.user.UserAuthorizationResponse;
import ru.rosatom.e2e.user.UserEndpoints;
import ru.rosatom.e2e.user.UserSearchResponse;

@DisplayName("User tests")
public class UsersTests extends BasicHttpTest {

    UserAuthorizationResponse userOrg1Auth = getContextValue(Environment.USER_FEDOTOV_AUTHORIZATION);

    @Test
    @DisplayName("Get all users")
    public void getAllUsers() {
        testGetAllUsersSuccess();
    }

    @Test
    @DisplayName("Get user with id")
    public void getUsersWithId() {
        getUserWithId(new UserSearchRequestId(1));
    }

    @Test
    @DisplayName("Get user with incorrect id")
    public void getUsersWithIncorrectId() {
        getUserWithIncorrectId(new UserSearchRequestId(-2));
    }

    @Test
    @DisplayName("Get user with phone")
    public void getUsersWithPhone() {
        getUserWithPhone(new UserSearchRequestPhone(" "));
    }

    @Test
    @DisplayName("Get user with email")
    public void getUsersWithEmail() {
        getUserWithEmail(new UserSearchRequestEmail("Рос"));
    }

    @Test
    @DisplayName("Get user with passport")
    public void getUsersWithPassport() {
        getUserWithPassport(new UserSearchRequestPassport("Рос"));
    }

    private WebTestClient.ResponseSpec getResponseSpecAllUsers() {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(UserEndpoints.USER_SEARCH.getUrl())
                .exchange();
    }

    private void testGetAllUsersSuccess() {
        getResponseSpecAllUsers().
                expectStatus().isOk().
                expectBody(UserSearchResponse.class).
                value(userSearchResponse -> userSearchResponse.getUsers().forEach(user -> {
                    Assertions.assertNotNull(user);
                    Assertions.assertNotNull(user.getId());
                    Assertions.assertNotNull(user.getOrganization());
                    Assertions.assertNotNull(user.getRole());
                    Assertions.assertNotNull(user.getPost());
                    Assertions.assertNotNull(user.getEmail());
                    Assertions.assertNotNull(user.getLastName());
                    Assertions.assertNotNull(user.getFirstName());
                    Assertions.assertNotNull(user.getPhone());
                    Assertions.assertNotNull(user.getPatronymic());
                    Assertions.assertNotNull(user.getDateOfBirth());
                }));

    }

    private WebTestClient.ResponseSpec getResponseSpecUserWithId(UserSearchRequestId userSearchRequestId) {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(UserEndpoints.USER_SEARCH + "/" + userSearchRequestId.getId())
                        .build())
                .exchange();
    }

    private void getUserWithId(UserSearchRequestId userSearchRequestId) {
        getResponseSpecUserWithId(userSearchRequestId)
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    Assertions.assertNotNull(user);
                    Assertions.assertEquals(user.getId(), userSearchRequestId.getId());
                    Assertions.assertNotNull(user.getOrganization());
                    Assertions.assertNotNull(user.getRole());
                    Assertions.assertNotNull(user.getPost());
                    Assertions.assertNotNull(user.getEmail());
                    Assertions.assertNotNull(user.getLastName());
                    Assertions.assertNotNull(user.getFirstName());
                    Assertions.assertNotNull(user.getPhone());
                    Assertions.assertNotNull(user.getPatronymic());
                    Assertions.assertNotNull(user.getDateOfBirth());
                }).returnResult();
    }

    private void getUserWithIncorrectId(UserSearchRequestId userSearchRequestId) {
        getResponseSpecUserWithId(userSearchRequestId)
                .expectStatus().isNotFound()
                        .expectBody(User.class)
                                .value(Assertions::assertNotNull).returnResult();
    }

    private WebTestClient.ResponseSpec getResponseSpecAllUserWithPhone(UserSearchRequestPhone userSearchRequestPhone) {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(UserEndpoints.USER_SEARCH_PHONE + userSearchRequestPhone.getPhone())
                        .build())
                .exchange();
    }

    private void getUserWithPhone(UserSearchRequestPhone userSearchRequestPhone) {
        getResponseSpecAllUserWithPhone(userSearchRequestPhone)
                .expectStatus().isOk()
                        .expectBodyList(User.class)
                                .value(response -> {
                                    Assertions.assertNotNull(response);
                                    for (User user : response) {
                                        Assertions.assertEquals(user.getPhone(), userSearchRequestPhone.getPhone());
                                        Assertions.assertNotNull(user.getOrganization());
                                        Assertions.assertNotNull(user.getRole());
                                        Assertions.assertNotNull(user.getPost());
                                        Assertions.assertNotNull(user.getEmail());
                                        Assertions.assertNotNull(user.getLastName());
                                        Assertions.assertNotNull(user.getFirstName());
                                        Assertions.assertNotNull(user.getId());
                                        Assertions.assertNotNull(user.getPatronymic());
                                        Assertions.assertNotNull(user.getDateOfBirth());
                                    }
                                }).returnResult();
    }

    private WebTestClient.ResponseSpec getResponseSpecAllUserWithEmail(UserSearchRequestEmail userSearchRequestEmail) {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(UserEndpoints.USER_SEARCH_EMAIL + userSearchRequestEmail.getEmail())
                        .build())
                .exchange();
    }

    private void getUserWithEmail(UserSearchRequestEmail userSearchRequestEmail) {
        getResponseSpecAllUserWithEmail(userSearchRequestEmail)
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    for (User user : response) {
                        Assertions.assertEquals(user.getEmail(), userSearchRequestEmail.getEmail());
                        Assertions.assertNotNull(user.getOrganization());
                        Assertions.assertNotNull(user.getRole());
                        Assertions.assertNotNull(user.getPost());
                        Assertions.assertNotNull(user.getPhone());
                        Assertions.assertNotNull(user.getLastName());
                        Assertions.assertNotNull(user.getFirstName());
                        Assertions.assertNotNull(user.getId());
                        Assertions.assertNotNull(user.getPatronymic());
                        Assertions.assertNotNull(user.getDateOfBirth());
                    }
                }).returnResult();
    }

    private WebTestClient.ResponseSpec getResponseSpecAllUserWithPassport(UserSearchRequestPassport userSearchRequestPassport) {
        return withAuthClient(userOrg1Auth)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(UserEndpoints.USER_SEARCH_PASSPORT + userSearchRequestPassport.getPassport())
                        .build())
                .exchange();
    }

    private void getUserWithPassport(UserSearchRequestPassport userSearchRequestPassport) {
        getResponseSpecAllUserWithPassport(userSearchRequestPassport)
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    for (User user : response) {
                        Assertions.assertNotNull(user.getEmail());
                        Assertions.assertNotNull(user.getOrganization());
                        Assertions.assertNotNull(user.getRole());
                        Assertions.assertNotNull(user.getPost());
                        Assertions.assertNotNull(user.getPhone());
                        Assertions.assertNotNull(user.getLastName());
                        Assertions.assertNotNull(user.getFirstName());
                        Assertions.assertNotNull(user.getId());
                        Assertions.assertNotNull(user.getPatronymic());
                        Assertions.assertNotNull(user.getDateOfBirth());
                    }
                }).returnResult();
    }
}
