package ru.rosatom.e2e.userTests.P1000_Authorization;

import ru.rosatom.e2e.user.UserAuthorizationRequest;

public class AdminAuthRequest  extends UserAuthorizationRequest {

    public AdminAuthRequest() {
        super("admin@mail.ru", "251323Nn");

    }
}
