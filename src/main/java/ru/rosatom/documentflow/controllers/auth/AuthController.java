package ru.rosatom.documentflow.controllers.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.UserCredentialsDto;
import ru.rosatom.documentflow.services.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> login(@RequestBody UserCredentialsDto userCredentialsDto) {
        log.info("Received a request to login user with email = {}", userCredentialsDto.getEmail());
        return authService.loginUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        log.info("Received a request to get info about user with email = {}", authentication.getName());
        return authService.userInfo(authentication);
    }
}
