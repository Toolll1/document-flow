package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.mappers.UserPassportMapper;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserOrganizationService organizationService;
    private final UserPassportServiceImpl passportService;
    private final UserPassportMapper passportMapper;
    private final UserMapper userMapper;
    private final UserRepository repository;

    @Override
    public UserReplyDto createUser(UserCreateDto dto) {

        UserOrganization organization = organizationService.getOrganization(dto.getOrganizationId());
        UserPassport passport = passportService.createPassport(passportMapper.dtoToObject(dto));
        User user = repository.save(userMapper.dtoToObject(dto, organization, passport));

        return userMapper.objectToReplyDto(user);
    }

    @Override
    public UserReplyDto updateUser(UserCreateDto dto, Long userId) {
        return null;
    }

    @Override
    public UserReplyDto getUser(Long userId) {
        return null;
    }

    @Override
    public List<UserReplyDto> getUsers(List<Long> ids, Integer from, Integer size) {
        return null;
    }

    @Override
    public UserReplyDto getUserByPhone(String phone) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
