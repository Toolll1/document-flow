package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.AuthTokenDto;
import ru.rosatom.documentflow.dto.UserCredentialsDto;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
@Tag(name = "Авторизация")
public class AuthController {

    private final AuthService authService;

    private final UserMapper userMapper;

    @Operation(summary = "Авторизация")
    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthTokenDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody @Parameter(description = "Учетные данные пользователя")
                                   UserCredentialsDto userCredentialsDto) {
        log.info("Received a request to login user with email = {}", userCredentialsDto.getEmail());
        return ResponseEntity.ok(authService.loginUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword()));
    }

    @Operation(summary = "Получить информацию о пользователе по токену авторизации")
    @GetMapping("/info")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthTokenDto.class)))
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("Received a request to get info about user with email = {}", user.getEmail());
        return ResponseEntity.ok(userMapper.objectToReplyDto(user));
    }
}
