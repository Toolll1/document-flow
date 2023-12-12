package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.mappers.UserPassportMapper;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;
    private final UserOrganizationService organizationService;
    private final UserPassportMapper passportMapper;
    private final UserMapper userMapper;

    @Operation(summary = "Добавить пользователя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    public UserReplyDto createUser(@Valid @RequestBody UserCreateDto dto) {

        log.info("Received a request to create a user " + dto);

        UserOrganization organization = organizationService.getOrganization(dto.getOrganizationId());
        UserPassport passport = passportMapper.dtoToObject(dto);
        User user = userMapper.dtoToObject(dto, organization, passport);

        return userMapper.objectToReplyDto(userService.createUser(organization, passport, user));
    }

    @Operation(summary = "Изменить пользователя")
    @PatchMapping("/{userId}")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto updateUser(
            @Valid @RequestBody UserUpdateDto dto,
            @PathVariable @Parameter(description = "ID пользователя") Long userId) {

        log.info("Received a request to update a user {}. userId = {}", dto, userId);

        return userMapper.objectToReplyDto(userService.updateUser(dto, userId));
    }

    @Operation(summary = "Установить пароль для пользователя")
    @PreAuthorize("(#userId==#user.id && hasAuthority('USER')) || hasAuthority('ADMIN')")
    @PatchMapping("/password/{userId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> setUserPassword(
            @Valid
            @Size(min = 8, message = "password is too short")
            @RequestParam(value = "password")
            @Parameter(description = "Пароль пользователя")
            String password,
            @PathVariable @Parameter(description = "ID пользователя") Long userId,
            @AuthenticationPrincipal User user) {
        log.info("Received a request to set password to user with userId = {}", userId);
        if (userService.setPasswordToUser(password, userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User with id " + userId + " not found", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Получить пользователя по ID")
    @PreAuthorize("(#userId==#user.id && hasAuthority('USER')) || hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto getUser(
            @PathVariable @Parameter(description = "ID пользователя") Long userId,
            @AuthenticationPrincipal User user) {

        log.info("A request was received to search for a user with an id {}", userId);

        return userMapper.objectToReplyDto(userService.getUser(userId));
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    public Page<UserReplyDto> getAllUsers(@ParameterObject
                                          @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
                                          @Parameter(description = "ID организации") Pageable pageable) {

        log.info("A search request was received for all users");

        return userService.getAllUsers(pageable)
                .map(userMapper::objectToReplyDto);
    }

    @Operation(summary = "Получить всех пользователей с сортировкой и пагинацией")
    @GetMapping("/ids")
    @SecurityRequirement(name = "JWT")
    public Page<UserReplyDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
            @Parameter(description = "ID организации") Pageable pageable) {

        log.info(
                "Received a request to search for all users for params: ids {}, sort {}, from {}, size {}",
                ids,
                pageable.getSort().get(),
                pageable.getPageNumber(),
                pageable.getPageSize());
        return userService.getUsers(ids, pageable)
                .map(userMapper::objectToReplyDto);
    }

    @Operation(summary = "Получить пользователя по номеру телефона")
    @GetMapping("/phone/{phone}")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto getUserByPhone(
            @PathVariable @Parameter(description = "Телефон пользователя") String phone) {

        log.info("Received a request to search user for telephone {}", phone);

        return userMapper.objectToReplyDto(userService.getUserByPhone(phone));
    }

    @Operation(summary = "Получить пользователя по eMail")
    @GetMapping("/email/{email}")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto getUserByEmail(
            @PathVariable @Parameter(description = "eMail пользователя") String email) {

        log.info("Received a request to search user for email {}", email);

        return userMapper.objectToReplyDto(userService.getUserByEmail(email));
    }

    @Operation(summary = "Получить пользователя по паспорту")
    @GetMapping("/passport/{passport}")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto getUserByPassport(
            @PathVariable @Parameter(description = "Паспорт пользователя") String passport) {

        log.info("Received a request to search user for passport {}", passport);

        return userMapper.objectToReplyDto(userService.getUserByPassport(passport));
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "JWT")
    public void deleteUser(@PathVariable @Parameter(description = "ID пользователя") Long userId) {

        log.info("Received a request to delete a user with an id " + userId);

        userService.deleteUser(userId);
    }
}
