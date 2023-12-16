package ru.rosatom.documentflow.services;

import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> loginUser(String email, String password);

}
