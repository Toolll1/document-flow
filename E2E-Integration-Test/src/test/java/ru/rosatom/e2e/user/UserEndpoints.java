package ru.rosatom.e2e.user;

import lombok.Getter;
import ru.rosatom.e2e.BasicEndpointProvider;

@Getter
public enum UserEndpoints implements BasicEndpointProvider {

    AUTHORIZATION("/v2/auth/login"),
    AUTHORIZATION_INFO("/v2/auth/info");

    private final String url;

    UserEndpoints(String url) {
        this.url = url;
    }

}
