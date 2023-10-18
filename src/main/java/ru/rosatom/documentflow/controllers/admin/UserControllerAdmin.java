package ru.rosatom.documentflow.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserControllerAdmin {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserReplyDto createUser(@Valid @RequestBody UserCreateDto dto) {

        log.info("Received a request to create a user " + dto);

        return service.createUser(dto);
    }

    @PatchMapping("/{userId}")
    public UserReplyDto updateUser(@Valid @RequestBody UserUpdateDto dto,
                                   @PathVariable Long userId) {

        log.info("Received a request to update a user {}. userId = {}", dto, userId);

        return service.updateUser(dto, userId);
    }

    @PutMapping("/password/{userId}")
    public ResponseEntity<?> setUserPassword(@Valid @Size(min = 8, message = "password is too short") @RequestParam(required = true, value = "password") String password,
                                             @PathVariable Long userId) {
        log.info("Received a request to set password to user with userId = {}", userId);
        if (service.setPasswordToUser(password, userId)) {
            return ResponseEntity.ok("Password has been set to user with id " + userId);
        } else {
            return new ResponseEntity<>("User with id " + userId + " not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{userId}")
    public UserReplyDto getUserDto(@PathVariable Long userId) {

        log.info("A request was received to search for a user with an id {}", userId);

        return service.getUserDto(userId);
    }

    @GetMapping("/ids")
    public List<UserReplyDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                       @RequestParam(value = "sort", defaultValue = "") String sort,  //например, сортировка по id или по фамилии (ID, LAST_NAME)
                                       @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {

        log.info("Received a request to search for all users for params: ids {}, sort {}, from {}, size {}", ids, sort, from, size);

        return service.getUsers(ids, sort.toUpperCase(), from, size);
    }

    @GetMapping("/phone/{phone}")
    public UserReplyDto getUserByPhone(@PathVariable String phone) {

        log.info("Received a request to search user for telephone {}", phone);

        return service.getUserByPhone(phone);
    }

    @GetMapping("/email/{email}")
    public UserReplyDto getUserByEmail(@PathVariable String email) {

        log.info("Received a request to search user for email {}", email);

        return service.getUserByEmail(email);
    }

    @GetMapping("/passport/{passport}")
    public UserReplyDto getUserByPassport(@PathVariable String passport) {

        log.info("Received a request to search user for passport {}", passport);

        return service.getUserByPassport(passport);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {

        log.info("Received a request to delete a user with an id " + userId);

        service.deleteUser(userId);
    }
}
