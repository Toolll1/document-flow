package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;

import java.util.List;

public interface UserService {
    UserReplyDto createUser(UserCreateDto dto);

    UserReplyDto updateUser(UserCreateDto dto, Long userId);

    UserReplyDto getUser(Long userId);

    List<UserReplyDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserReplyDto getUserByPhone(String phone);

    void deleteUser(Long userId);
}
