package ru.rosatom.e2e.user;

import lombok.Getter;
import ru.rosatom.e2e.BasicEndpointProvider;

@Getter
public enum UserEndpoints implements BasicEndpointProvider {

    AUTHORIZATION("/v2/auth/login"),
    AUTHORIZATION_INFO("/v2/auth/info"),
    USER_SEARCH("/v2/users"),
    USER_SEARCH_PHONE("/v2/users/phone"),
    USER_SEARCH_EMAIL("/v2/users/email"),
    USER_SEARCH_PASSPORT("/v2/users/passport");

    private final String url;

    UserEndpoints(String url) {
        this.url = url;
    }

}
