package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.adapters.DateTimeAdapter;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.mappers.UserPassportMapper;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.models.UserRole;
import ru.rosatom.documentflow.repositories.UserPassportRepository;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserOrganizationService organizationService;
    private final UserPassportMapper passportMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserPassportRepository passportRepository;

    @Override
    public UserReplyDto createUser(UserCreateDto dto) {

        checkUnique(dto);

        UserOrganization organization = organizationService.getOrganization(dto.getOrganizationId());
        UserPassport passport = passportRepository.save(passportMapper.dtoToObject(dto));
        User user = userRepository.save(userMapper.dtoToObject(dto, organization, passport));

        return userMapper.objectToReplyDto(user);
    }

    @Override
    public UserReplyDto updateUser(UserUpdateDto dto, Long userId) {

        checkUnique(dto, userId);

        User user = getUser(userId);
        UserPassport passport = user.getPassport();

        passport.setSeries(defaultIfNull(dto.getPassportSeries(), passport.getSeries()));
        passport.setNumber(defaultIfNull(dto.getPassportNumber(), passport.getNumber()));
        passport.setIssued(defaultIfNull(dto.getPassportIssued(), passport.getIssued()));
        passport.setDate(defaultIfNull(DateTimeAdapter.stringToDate(dto.getPassportDate()), passport.getDate()));
        passport.setKp(defaultIfNull(dto.getPassportKp(), passport.getKp()));
        user.setFirstName(defaultIfNull(dto.getFirstName(), user.getFirstName()));
        user.setLastName(defaultIfNull(dto.getLastName(), user.getLastName()));
        user.setPatronymic(defaultIfNull(dto.getPatronymic(), user.getPatronymic()));
        user.setDateOfBirth(defaultIfNull(DateTimeAdapter.stringToDate(dto.getDateOfBird()), user.getDateOfBirth()));
        user.setEmail(defaultIfNull(dto.getEmail(), user.getEmail()));
        user.setPhone(defaultIfNull(dto.getPhone(), user.getPhone()));
        user.setPost(defaultIfNull(dto.getPost(), user.getPost()));
        user.setRole(defaultIfNull(UserRole.valueOf(dto.getRole()), user.getRole()));
        user.setOrganization((dto.getOrganizationId() == null) ? user.getOrganization() : organizationService.getOrganization(dto.getOrganizationId()));
        user.setPassport(passport);

        return userMapper.objectToReplyDto(userRepository.save(user));
    }

    @Override
    public UserReplyDto getUserDto(Long userId) {

        return userMapper.objectToReplyDto(getUser(userId));
    }

    @Override
    public List<UserReplyDto> getUsers(List<Long> ids, String sort, Integer from, Integer size) {

        PageRequest pageable = pageableCreator(from, size, sort);

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable)
                    .stream()
                    .map(userMapper::objectToReplyDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, pageable)
                    .stream()
                    .map(userMapper::objectToReplyDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserReplyDto getUserByPhone(String phone) {

        User user = userRepository.findByPhone(phone).orElseThrow(() -> new ObjectNotFoundException("There is no user with this phone"));

        return userMapper.objectToReplyDto(user);
    }

    @Override
    public void deleteUser(Long userId) {

        getUser(userId);

        userRepository.deleteById(userId);
    }

    @Override
    public UserReplyDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("There is no user with this email"));

        return userMapper.objectToReplyDto(user);
    }

    @Override
    public UserReplyDto getUserByPassport(String passport) {

        String pass = passport.replaceAll(" ", "");

        try {
            Integer.parseInt(pass);
        } catch (NumberFormatException e) {
            throw new BadRequestException("input error - only numbers should be in the passport");
        }

        if (pass.length() != 10) {
            throw new BadRequestException("input error - incorrect length");
        }

        User user = userRepository.findByPassportSeriesAndPassportNumber(pass.substring(0, 4), pass.substring(4))
                .orElseThrow(() -> new ObjectNotFoundException("There is no user with this passport"));

        return userMapper.objectToReplyDto(user);
    }

    private void checkUnique(UserCreateDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        if (userRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new ConflictException("Пользователь с таким телефоном уже существует");
        }
        if (userRepository.findByPassportSeriesAndPassportNumber(dto.getPassportSeries(), dto.getPassportNumber()).isPresent()) {
            throw new ConflictException("Пользователь с таким паспортом уже существует");
        }
    }

    private void checkUnique(UserUpdateDto dto, Long userId) {

        Optional<User> userToEmail = userRepository.findByEmail(dto.getEmail());
        Optional<User> userToPhone = userRepository.findByPhone(dto.getPhone());
        Optional<User> userToPassport = userRepository.findByPassportSeriesAndPassportNumber(dto.getPassportSeries(), dto.getPassportNumber());

        if (userToEmail.isPresent() && !userToEmail.get().getId().equals(userId)) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        if (userToPhone.isPresent() && !userToPhone.get().getId().equals(userId)) {
            throw new ConflictException("Пользователь с таким телефоном уже существует");
        }
        if (userToPassport.isPresent() && !userToPassport.get().getId().equals(userId)) {
            throw new ConflictException("Пользователь с таким паспортом уже существует");
        }
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    private PageRequest pageableCreator(Integer from, Integer size, String sort) {

        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(from / size, size);
        }

        switch (sort) {
            case "ID":
                return PageRequest.of(from / size, size, Sort.by("id"));
            case "LAST_NAME":
                return PageRequest.of(from / size, size, Sort.by("lastName"));
            default:
                throw new BadRequestException("Unknown sort: " + sort);
        }
    }

    private User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("There is no user with this id"));
    }
}
