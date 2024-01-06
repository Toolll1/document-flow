package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.AuthTokenDto;

public interface AuthService {

    AuthTokenDto loginUser(String email, String password);

}
