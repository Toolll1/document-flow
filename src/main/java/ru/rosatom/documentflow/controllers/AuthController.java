package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.AuthTokenDto;
import ru.rosatom.documentflow.dto.UserCredentialsDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
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
    public AuthTokenDto login(@RequestBody @Parameter(description = "Учетные данные пользователя")
                                   UserCredentialsDto userCredentialsDto) {
        log.info("Received a request to login user with email = {}", userCredentialsDto.getEmail());
        return authService.loginUser(userCredentialsDto.getEmail(),userCredentialsDto.getPassword());
    }

    @Operation(summary = "Получить информацию о пользователе по токену авторизации")
    @GetMapping("/info")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto getUserInfo(@AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("Received a request to get info about user with email = {}", user.getEmail());
        return userMapper.objectToReplyDto(user);
    }
}
