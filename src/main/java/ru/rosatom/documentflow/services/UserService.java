package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserReplyDto createUser(UserCreateDto dto);

    UserReplyDto updateUser(UserUpdateDto dto, Long userId);

    UserReplyDto getUserDto(Long userId);

    List<UserReplyDto> getUsers(List<Long> ids, String sort, Integer from, Integer size);

    UserReplyDto getUserByPhone(String phone);

    void deleteUser(Long userId);

    UserReplyDto getUserByEmail(String email);

    UserReplyDto getUserByPassport(String passport);
}
