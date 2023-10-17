package ru.rosatom.documentflow.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.rosatom.documentflow.dto.UserCredentialsDto;

public interface AuthService {

    ResponseEntity<?> loginUser(UserCredentialsDto userCredentialsDto);

    ResponseEntity<?> userInfo(Authentication authentication);
}
