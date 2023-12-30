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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserOrganizationService organizationService;
    private final PasswordEncoder passwordEncoder;
    private final UserPassportRepository passportRepository;
    private final UserRepository userRepository;

    /**
     * Создает нового пользователя в системе.
     *
     * @param organization Организация, к которой будет привязан пользователь.
     * @param passport     Паспортные данные пользователя.
     * @param user         Информация о пользователе.
     * @return Созданный пользователь.
     * @throws ConflictException если пользователь с такими данными уже существует.
     */
    @Override
    public User createUser(UserOrganization organization, UserPassport passport, User user) {

        checkUnique(user);

        passportRepository.save(passport);

        return userRepository.save(user);
    }

    /**
     * Обновляет информацию о существующем пользователе.
     *
     * @param dto    Объект передачи данных с обновленной информацией пользователя.
     * @param userId Идентификатор пользователя, которого нужно обновить.
     * @return Обновленный пользователь.
     * @throws ObjectNotFoundException если пользователь с указанным идентификатором не найден.
     */
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

    /**
     * Получает пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Найденный пользователь.
     * @throws ObjectNotFoundException если пользователь не найден.
     */
    public User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("There is no user with this id"));
    }

    /**
     * Получает страницу пользователей по списку идентификаторов.
     * <p>
     * Если список идентификаторов пустой или равен null, возвращает всех пользователей.
     * В противном случае возвращает пользователей, соответствующих предоставленным идентификаторам.
     *
     * @param ids      список идентификаторов пользователей для поиска.
     * @param pageable параметры пагинации.
     * @return страница пользователей соответствующих условиям поиска.
     */
    @Override
    public Page<User> getUsers(List<Long> ids, Pageable pageable) {

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findAllByIdIn(ids, pageable);
        }
    }

    /**
     * Получает пользователя по его телефонному номеру.
     * <p>
     * Метод выполняет поиск пользователя по указанному телефонному номеру.
     * В случае отсутствия пользователя с таким номером выбрасывает исключение ObjectNotFoundException.
     *
     * @param phone телефонный номер пользователя для поиска.
     * @return объект пользователя, соответствующий указанному телефонному номеру.
     * @throws ObjectNotFoundException если пользователь с таким телефоном не найден.
     */
    @Override
    public User getUserByPhone(String phone) {

        return userRepository.findByPhone(phone).orElseThrow(() -> new ObjectNotFoundException("There is no user with this phone"));
    }

    /**
     * Удаляет пользователя по его идентификатору.
     * <p>
     * Перед удалением проверяет наличие пользователя в системе.
     * В случае отсутствия пользователя выбрасывает исключение ObjectNotFoundException.
     *
     * @param userId идентификатор пользователя для удаления.
     * @throws ObjectNotFoundException если пользователь не найден.
     */
    @Override
    public void deleteUser(Long userId) {

        getUser(userId);

        userRepository.deleteById(userId);
    }

    /**
     * Получает пользователя по его электронной почте.
     * <p>
     * Метод выполняет поиск пользователя по указанному адресу электронной почты.
     * В случае отсутствия пользователя с таким адресом выбрасывает исключение ObjectNotFoundException.
     *
     * @param email адрес электронной почты пользователя для поиска.
     * @return объект пользователя, соответствующий указанному адресу электронной почты.
     * @throws ObjectNotFoundException если пользователь с таким email не найден.
     */
    @Override
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("There is no user with this email"));
    }

    /**
     * Получает пользователя по серии и номеру паспорта.
     * <p>
     * Метод выполняет поиск пользователя по серии и номеру паспорта после очистки и проверки формата.
     * В случае отсутствия пользователя или некорректного формата данных выбрасывает исключение BadRequestException.
     *
     * @param passport серия и номер паспорта для поиска.
     * @return объект пользователя, соответствующего указанным данным паспорта.
     * @throws BadRequestException если данные паспорта некорректны или пользователь не найден.
     */
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

    /**
     * Устанавливает пароль пользователю.
     * <p>
     * Метод кодирует предоставленный пароль и устанавливает его пользователю с указанным идентификатором.
     * Возвращает true, если операция прошла успешно, и false, если пользователь не найден.
     *
     * @param password новый пароль пользователя.
     * @param id       идентификатор пользователя, которому устанавливается пароль.
     * @return true, если пароль успешно установлен; false, если пользователь не найден.
     */
    @Override
    public boolean setPasswordToUser(String password, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    /**
     * Получает всех пользователей в системе с пагинацией.
     *
     * @param pageable параметры пагинации.
     * @return страница со всеми пользователями.
     */
    @Override
    public Page<User> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    /**
     * Находит всех пользователей, принадлежащих к определенной организации.
     *
     * @param id идентификатор организации.
     * @return список пользователей, принадлежащих к указанной организации.
     */
    @Override
    public List<User> findAllByOrganizationId(Long id) {
        return userRepository.findAllByOrganizationId(id);
    }

    /**
     * Проверяет уникальность пользователя в системе.
     * <p>
     * Метод проверяет, существует ли уже пользователь с теми же email, телефоном или паспортными данными.
     * Если существует, выбрасывает исключение ConflictException.
     *
     * @param user объект пользователя, который необходимо проверить.
     * @throws ConflictException если пользователь с такими данными уже существует.
     */
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

    /**
     * Проверяет уникальность обновленных данных пользователя.
     * <p>
     * Метод проверяет, не используются ли обновленные данные (email, телефон, паспортные данные) другим пользователем.
     * Если используются, выбрасывает исключение ConflictException.
     *
     * @param dto    объект DTO с обновленной информацией пользователя.
     * @param userId идентификатор пользователя, для которого происходит обновление.
     * @throws ConflictException если обновленные данные уже используются другим пользователем.
     */
    private void checkUnique(UserUpdateDto dto, Long userId) {

        userRepository.findByEmail(dto.getEmail())
                .filter(user -> !user.getId().equals(userId))
                .ifPresent(u -> {
                    throw new ConflictException("Пользователь с таким email уже существует");
                });
        userRepository.findByPhone(dto.getPhone())
                .filter(user -> !user.getId().equals(userId))
                .ifPresent(u -> {
                    throw new ConflictException("Пользователь с таким телефоном уже существует");
                });
        userRepository.findByPassportSeriesAndPassportNumber(dto.getPassportSeries(), dto.getPassportNumber())
                .filter(user -> !user.getId().equals(userId))
                .ifPresent(u -> {
                    throw new ConflictException("Пользователь с таким паспортом уже существует");
                });
    }

    /**
     * Возвращает значение по умолчанию, если предоставленное значение равно null.
     *
     * @param <T>          тип возвращаемого значения.
     * @param value        значение, которое необходимо проверить.
     * @param defaultValue значение по умолчанию, если value равно null.
     * @return value, если оно не равно null; в противном случае возвращает defaultValue.
     */
    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Создает объект PageRequest для пагинации.
     *
     * @param from начальный индекс страницы.
     * @param size размер страницы.
     * @param sort параметр сортировки.
     * @return объект PageRequest, сконфигурированный с заданными параметрами.
     */
    private PageRequest pageableCreator(Integer from, Integer size, String sort) {
        Sort sortBy = !sort.isEmpty() ? Sort.by(sort) : Sort.unsorted();
        return PageRequest.of(from / size, size, sortBy);
    }

    /**
     * Проверяет, имеет ли пользователь право на выполнение действия на основе его принадлежности к организации.
     *
     * @param id   идентификатор пользователя, чьи права проверяются.
     * @param user объект пользователя, для которого производится проверка.
     * @return true, если пользователь имеет право на действие; false в противном случае.
     */
    public boolean isAllowed(Long id, User user) {
        return Objects.equals(getUser(id).getOrganization().getId(), user.getOrganization().getId());
    }
}
