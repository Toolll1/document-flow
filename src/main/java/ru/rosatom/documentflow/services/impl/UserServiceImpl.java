package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.adapters.DateTimeAdapter;
import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.models.UserRole;
import ru.rosatom.documentflow.repositories.UserPassportRepository;
import ru.rosatom.documentflow.repositories.UserRepository;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserOrganizationService organizationService;
    private final PasswordEncoder passwordEncoder;
    private final UserPassportRepository passportRepository;
    private final UserRepository userRepository;

    @Override
    public User createUser(UserOrganization organization, UserPassport passport, User user) {

        checkUnique(user);

        passportRepository.save(passport);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateDto dto, Long userId) {

        checkUnique(dto, userId);

        User user = getUser(userId);
        UserPassport passport = user.getPassport();

        passport.setSeries(defaultIfNull(dto.getPassportSeries(), passport.getSeries()));
        passport.setNumber(defaultIfNull(dto.getPassportNumber(), passport.getNumber()));
        passport.setIssued(defaultIfNull(dto.getPassportIssued(), passport.getIssued()));
        passport.setKp(defaultIfNull(dto.getPassportKp(), passport.getKp()));
        passport.setDate((dto.getPassportDate() != null) ? DateTimeAdapter.stringToDate(dto.getPassportDate()) : passport.getDate());

        user.setFirstName(defaultIfNull(dto.getFirstName(), user.getFirstName()));
        user.setLastName(defaultIfNull(dto.getLastName(), user.getLastName()));
        user.setPatronymic(defaultIfNull(dto.getPatronymic(), user.getPatronymic()));
        user.setDateOfBirth((dto.getDateOfBirth() != null) ? DateTimeAdapter.stringToDate(dto.getDateOfBirth()) : user.getDateOfBirth());
        user.setEmail(defaultIfNull(dto.getEmail(), user.getEmail()));
        user.setPhone(defaultIfNull(dto.getPhone(), user.getPhone()));
        user.setPost(defaultIfNull(dto.getPost(), user.getPost()));
        user.setRole((dto.getRole() != null) ? UserRole.valueOf(dto.getRole()) : user.getRole());
        user.setOrganization((dto.getOrganizationId() == null) ? user.getOrganization() : organizationService.getOrganization(dto.getOrganizationId()));
        user.setPassport(passport);

        return user;
    }

    public User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("There is no user with this id"));
    }

    @Override
    public Page<User> getUsers(List<Long> ids, Pageable pageable) {

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findAllByIdIn(ids, pageable);
        }
    }

    @Override
    public User getUserByPhone(String phone) {

        return userRepository.findByPhone(phone).orElseThrow(() -> new ObjectNotFoundException("There is no user with this phone"));
    }

    @Override
    public void deleteUser(Long userId) {

        getUser(userId);

        userRepository.deleteById(userId);
    }

    @Override
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("There is no user with this email"));
    }

    @Override
    public User getUserByPassport(String passport) {

        String pass = passport.replaceAll(" ", "");

        try {
            Integer.parseInt(pass);
        } catch (NumberFormatException e) {
            throw new BadRequestException("input error - only numbers should be in the passport");
        }

        if (pass.length() != 10) {
            throw new BadRequestException("input error - incorrect length");
        }

        return userRepository.findByPassportSeriesAndPassportNumber(pass.substring(0, 4), pass.substring(4))
                .orElseThrow(() -> new ObjectNotFoundException("There is no user with this passport"));
    }

    @Override
    public boolean setPasswordToUser(String password, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findAllByOrganizationId(Long id) {
        return userRepository.findAllByOrganizationId(id);
    }

    private void checkUnique(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new ConflictException("Пользователь с таким телефоном уже существует");
        }
        if (userRepository.findByPassportSeriesAndPassportNumber(user.getPassport().getSeries(), user.getPassport().getNumber()).isPresent()) {
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
        Sort sortBy = !sort.isEmpty() ? Sort.by(sort) : Sort.unsorted();
        return PageRequest.of(from / size, size, sortBy);
    }

    public boolean isAllowed(Long id, User user) {
        boolean alllowed = Objects.equals(getUser(id).getOrganization().getId(), user.getOrganization().getId());
        return alllowed;
    }
}
