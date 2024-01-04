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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
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
    @PreAuthorize("hasAuthority('ADMIN') || #dto.organizationId==#user1.organization.id && hasAuthority('COMPANY_ADMIN')")
    public UserReplyDto createUser(@Valid @RequestBody UserCreateDto dto,
                                   @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user1) {

        log.info("Received a request to create a user " + dto);

        UserOrganization organization = organizationService.getOrganization(dto.getOrganizationId());
        UserPassport passport = passportMapper.dtoToObject(dto);
        User user = userMapper.dtoToObject(dto, organization, passport);

        return userMapper.objectToReplyDto(userService.createUser(organization, passport, user));
    }

    @Operation(summary = "Изменить пользователя")
    @PatchMapping("/{userId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || (@userServiceImpl.isAllowed(#userId, #user) || hasAuthority('COMPANY_ADMIN'))")
    public UserReplyDto updateUser(
            @Valid @RequestBody UserUpdateDto dto,
            @PathVariable @Parameter(description = "ID пользователя") Long userId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {

        log.info("Received a request to update a user {}. userId = {}", dto, userId);

        return userMapper.objectToReplyDto(userService.updateUser(dto, userId));
    }

    @Operation(summary = "Установить пароль для пользователя")
    @PreAuthorize("(#userId==#user.id && hasAuthority('USER')) || hasAuthority('ADMIN') || " +
            "(@userServiceImpl.isAllowed(#userId, #user) && hasAuthority('COMPANY_ADMIN'))")
    @PatchMapping("/password/{userId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> setUserPassword(
            @Valid
            @Size(min = 8, message = "password is too short")
            @RequestParam(value = "password")
            @Parameter(description = "Пароль пользователя")
            String password,
            @PathVariable @Parameter(description = "ID пользователя") Long userId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        log.info("Received a request to set password to user with userId = {}", userId);
        if (userService.setPasswordToUser(password, userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User with id " + userId + " not found", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Получить пользователя по ID")
    @PreAuthorize("(#userId==#user.id && hasAuthority('USER')) || hasAuthority('ADMIN') || " +
            "(@userServiceImpl.isAllowed(#userId, #user) && hasAuthority('COMPANY_ADMIN'))")
    @GetMapping("/{userId}")
    @SecurityRequirement(name = "JWT")
    public UserReplyDto getUser(
            @PathVariable @Parameter(description = "ID пользователя") Long userId,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {

        log.info("A request was received to search for a user with an id {}", userId);

        return userMapper.objectToReplyDto(userService.getUser(userId));
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || ((hasAuthority('COMPANY_ADMIN') || hasAuthority('USER')) " +
            "&& #orgId.isPresent() && #user.organization.id.equals(#orgId.get()))")
    public Page<UserReplyDto> getAllUsers(@ParameterObject
                                          @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
                                          @Parameter(description = "ID организации") Pageable pageable,
                                          @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user,
                                          @RequestParam(required = false, name = "org_id") @Parameter(description = "ID организации") Optional<Long> orgId) {

        log.info("A search request was received for all users");

        return orgId.map(aLong -> userService.findAllByOrganizationId(aLong, pageable)
                .map(userMapper::objectToReplyDto)).orElseGet(() -> userService.getAllUsers(pageable)
                .map(userMapper::objectToReplyDto));
    }


    @Operation(summary = "Получить всех пользователей с сортировкой и пагинацией")
    @GetMapping("/ids")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserReplyDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @ParameterObject
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
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
    @PostAuthorize("((returnObject.organization.id == #user.organization.id && hasAuthority('COMPANY_ADMIN')) " +
            "|| hasAuthority('ADMIN'))")
    public UserReplyDto getUserByPhone(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @PathVariable @Parameter(description = "Телефон пользователя") String phone) {

        log.info("Received a request to search user for telephone {}", phone);

        return userMapper.objectToReplyDto(userService.getUserByPhone(phone));
    }

    @Operation(summary = "Получить пользователя по eMail")
    @GetMapping("/email/{email}")
    @SecurityRequirement(name = "JWT")
    @PostAuthorize("((returnObject.organization.id == #user.organization.id && hasAuthority('COMPANY_ADMIN'))" +
            " || hasAuthority('ADMIN'))")
    public UserReplyDto getUserByEmail(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @PathVariable @Parameter(description = "eMail пользователя") String email) {

        log.info("Received a request to search user for email {}", email);

        return userMapper.objectToReplyDto(userService.getUserByEmail(email));
    }

    @Operation(summary = "Получить пользователя по паспорту")
    @GetMapping("/passport/{passport}")
    @SecurityRequirement(name = "JWT")
    @PostAuthorize("((returnObject.organization.id == #user.organization.id && hasAuthority('COMPANY_ADMIN')) " +
            "|| hasAuthority('ADMIN'))")
    public UserReplyDto getUserByPassport(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @PathVariable @Parameter(description = "Паспорт пользователя") String passport) {

        log.info("Received a request to search user for passport {}", passport);

        return userMapper.objectToReplyDto(userService.getUserByPassport(passport));
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || (@userServiceImpl.isAllowed(#userId, #user) && hasAuthority('COMPANY_ADMIN'))")
    public void deleteUser(@PathVariable @Parameter(description = "ID пользователя") Long userId,
                           @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {

        log.info("Received a request to delete a user with an id " + userId);

        userService.deleteUser(userId);
    }
}
