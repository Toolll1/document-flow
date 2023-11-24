package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.configuration.JWT.JWTUtil;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.AuthService;
import ru.rosatom.documentflow.services.CustomUserDetailsService;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;


    @Override
    public ResponseEntity<?> loginUser(String email, String password) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(jwtUtil.generateToken(email), HttpStatus.ACCEPTED);
        } catch (AuthenticationException authenticationException) {
            log.error("Authentication failed: ", authenticationException);
            return new ResponseEntity<>(authenticationException.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    public ResponseEntity<?> userInfo(Authentication authentication) {
        User user = (User) customUserDetailsService.loadUserByUsername(authentication.getName());
        return ResponseEntity.ok(userMapper.objectToReplyDto(user));
    }
}
