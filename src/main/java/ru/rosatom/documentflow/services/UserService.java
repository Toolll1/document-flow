package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;

import java.util.List;

public interface UserService {
    User createUser(UserOrganization organization, UserPassport passport, User user);

    User updateUser(UserUpdateDto dto, Long userId);

    User getUser(Long userId);

    List<User> getUsers(List<Long> ids, String sort, Integer from, Integer size);

    User getUserByPhone(String phone);

    void deleteUser(Long userId);

    User getUserByEmail(String email);

    User getUserByPassport(String passport);

    boolean setPasswordToUser(String password, Long id);

    List<User> getAllUsers();
}
