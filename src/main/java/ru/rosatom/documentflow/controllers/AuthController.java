package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Авторизация")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Авторизация")
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<?> login(@RequestBody @Parameter(description = "DTO Учетных данных пользователя") UserCredentialsDto userCredentialsDto) {
    log.info("Received a request to login user with email = {}", userCredentialsDto.getEmail());
    return authService.loginUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
  }

  @Operation(summary = "Получить информацию о пользователе по токену авторизации")
  @GetMapping("/info")
  @SecurityRequirement(name = "JWT")
  public ResponseEntity<?> getUserInfo(
      @Parameter(description = "Аутентификация", hidden = true) Authentication authentication) {
    log.info("Received a request to get info about user with email = {}", authentication.getName());
    return authService.userInfo(authentication);
  }
}
