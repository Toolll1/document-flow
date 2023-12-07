package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.configuration.JWT.JWTUtil;
import ru.rosatom.documentflow.dto.AppError;
import ru.rosatom.documentflow.dto.AuthTokenDto;
import ru.rosatom.documentflow.dto.UserWithoutPassportDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.AuthService;
import ru.rosatom.documentflow.services.UserService;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final UserService userService;


    @Override
    public ResponseEntity<?> loginUser(String email, String password) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException authenticationException) {
            log.error("Authentication failed: ", authenticationException);
            return new ResponseEntity<>(new AppError("Bad credentials"), HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByEmail(email);

        AuthTokenDto authToken = AuthTokenDto.builder()
                .token(jwtUtil.generateToken(email))
                .user(mapper.map(user, UserWithoutPassportDto.class))
                .build();

        return new ResponseEntity<>(authToken, HttpStatus.ACCEPTED);
    }

}