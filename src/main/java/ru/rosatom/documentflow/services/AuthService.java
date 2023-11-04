package ru.rosatom.documentflow.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    ResponseEntity<?> loginUser(String email, String password);

    ResponseEntity<?> userInfo(Authentication authentication);
}
