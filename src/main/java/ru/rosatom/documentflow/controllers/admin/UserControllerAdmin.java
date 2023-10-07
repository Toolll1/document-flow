package ru.rosatom.documentflow.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    public UserReplyDto updateUniversity(@RequestBody UserCreateDto dto,
                                         @PathVariable Long userId) {

        log.info("Received a request to update a user {}. userId = {}", dto, userId);

        return service.updateUser(dto, userId);
    }

    @GetMapping("/id/{userId}")
    public UserReplyDto getUser(@PathVariable Long userId) {

        log.info("A request was received to search for a user with an id {}", userId);

        return service.getUser(userId);
    }

    @GetMapping("/ids")
    public List<UserReplyDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                       @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {

        log.info("Received a request to search for all users for params: ids {}, from {}, size {}", ids, from, size);

        return service.getUsers(ids, from, size);
    }

    @GetMapping("/phone/{phone}")
    public UserReplyDto getUserByPhone(@PathVariable String phone) {

        log.info("Received a request to search user for telephone {}", phone);

        return service.getUserByPhone(phone);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {

        log.info("Received a request to delete a user with an id " + userId);

        service.deleteUser(userId);
    }
}
