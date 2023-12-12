package ru.rosatom.documentflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;

import java.util.List;

public interface UserService {
    User createUser(UserOrganization organization, UserPassport passport, User user);

    User updateUser(UserUpdateDto dto, Long userId);

    User getUser(Long userId);

    Page<User> getUsers(List<Long> ids, Pageable pageable);

    User getUserByPhone(String phone);

    void deleteUser(Long userId);

    User getUserByEmail(String email);

    User getUserByPassport(String passport);

    boolean setPasswordToUser(String password, Long id);

    Page<User> getAllUsers(Pageable pageable);

    List<User> findAllByOrganizationId(Long id);
}
