package ru.rosatom.documentflow.services.impl;
/*
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.rosatom.documentflow.dto.UserUpdateDto;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.models.UserRole;
import ru.rosatom.documentflow.repositories.UserPassportRepository;
import ru.rosatom.documentflow.repositories.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserPassportRepository userPassportRepository;
    private User user;
    private UserPassport userPassport;
    private UserOrganization userOrganization;

    @BeforeEach
    void setUp() {
        userPassport = new UserPassport(111L, "2222", "333333", "444", LocalDate.of(2020, 02, 10), "105");
        userOrganization = new UserOrganization();
        user = new User(11L, "Andrey", "Andreev", "Andreevich", LocalDate.of(2000, 02, 10),
                " qqq@mail.ru", "79262555555", "password", "post", UserRole.USER, userPassport, userOrganization);
    }

    @Test
    void createUser() {
        userService.createUser(userOrganization, userPassport, user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(userPassportRepository, Mockito.times(1)).save(userPassport);
    }

    @Test
    void createUserFalse() {
        userRepository.save(user);
        UserPassport userPassport2 = new UserPassport(111L, "2222", "333333", "444", LocalDate.of(2020, 02, 10), "105");
        UserOrganization userOrganization2 = new UserOrganization();
        User user2 = new User(11L, "Andrey", "Andreev", "Andreevich", LocalDate.of(2000, 02, 10),
                " qqq@mail.ru", "79262555555", "password", "post", UserRole.USER, userPassport2, userOrganization2);
        Mockito.when(userRepository.save(user2)).thenThrow(ConflictException.class);
    }

    @Test
    void updateUser() {
        UserUpdateDto dto = new UserUpdateDto(111L, "Kirill", "Kirillov", "Kirillovich",
                "02.02.2000", " qqq@mail.ru", "79262555333", "1212",
                "333444", "3333", "02.02.2022", "104", 1L,
                "USER", "post");
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Assert.assertEquals(Optional.of(user), userRepository.findById(user.getId()));
        Assert.assertEquals(userService.updateUser(dto, user.getId()), user);
    }

    @Test
    void getUser() {
        Mockito.when(userRepository.findById(111L)).thenReturn(Optional.of(user));
        Assert.assertEquals(Optional.of(user), userRepository.findById(111L));
    }

    @Test
    void getUserByPhone() {
        user.setPhone("79999999999");
        Mockito.when(userRepository.findByPhone("79999999999")).thenReturn(Optional.of(user));
        Assert.assertTrue(userRepository.findByPhone("79999999999").isPresent());
    }

    @Test
    void getUserByPhoneFalse() {
        user.setPhone("79999999999");
        Assert.assertFalse(userRepository.findByPhone("79999999").isPresent());
        Mockito.when(userRepository.findByPhone("79999999")).thenThrow(ObjectNotFoundException.class);
    }

    @Test
    void getUserByEmail() {
        user.setEmail("qqq@mail.ru");
        Mockito.when(userRepository.findByEmail("qqq@mail.ru")).thenReturn(Optional.of(user));
        Assert.assertTrue(userRepository.findByEmail("qqq@mail.ru").isPresent());

    }

    @Test
    void getUserByEmailFalse() {
        user.setEmail("qqq@mail.ru");
        Assert.assertFalse(userRepository.findByEmail("aaa@mail.ru").isPresent());
        Mockito.when(userRepository.findByEmail("aaa@mail.ru")).thenThrow(ObjectNotFoundException.class);
    }

    @Test
    void getUserByPassport() {
        userPassport.setNumber("222222");
        userPassport.setSeries("1111");
        user.setPassport(userPassport);
        Mockito.when(userRepository.findByPassportSeriesAndPassportNumber("1111", "222222"))
                .thenReturn(Optional.of(user));
    }

    @Test
    void getUserByPassportFalse() {
        userPassport.setNumber("222222");
        userPassport.setSeries("1111");
        user.setPassport(userPassport);
        Assert.assertFalse(userRepository.findByPassportSeriesAndPassportNumber
                ("2222", "333333").isPresent());
        Mockito.when(userRepository.findByPassportSeriesAndPassportNumber
                ("2222", "333333")).thenThrow(ObjectNotFoundException.class);
    }

    @Test
    void getAllUsers() {
        userService.getAllUsers();
        Mockito.verify(userRepository).findAll();
        List<User> users = new ArrayList<>(Arrays.asList(new User(), new User(), new User()));
        Mockito.when(userRepository.findAll()).thenReturn(users);
    }
}


 */