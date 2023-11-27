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
import ru.rosatom.documentflow.dto.AuthTokenDto;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.AuthService;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;

    private final ModelMapper mapper;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> loginUser(String email, String password) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(email);

            var user = userRepository.findByEmail(email).orElseThrow();
            AuthTokenDto authToken = new AuthTokenDto();
            mapper.map(user, authToken);
            authToken.setToken(token);
            /*return ResponseEntity.ok(AuthTokenDto.builder()
                    .dateOfBirth(String.valueOf(user.getDateOfBirth())).phone(user.getPhone())
                    .lastName(user.getLastName()).firstName(user.getFirstName()).patronymic(user.getPatronymic())
                    .post(user.getPost()).role(String.valueOf(user.getRole())).email(user.getEmail())
                    .organizationId(user.getOrganization().getId()).token(token).build());*/
            return ResponseEntity.ok(authToken);
        } catch (AuthenticationException authenticationException) {
            log.error("Authentication failed: ", authenticationException);
            return new ResponseEntity<>(authenticationException.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

}
