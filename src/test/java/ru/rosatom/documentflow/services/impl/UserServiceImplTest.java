//package ru.rosatom.documentflow.services.impl;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import ru.rosatom.documentflow.models.User;
//import ru.rosatom.documentflow.models.UserOrganization;
//import ru.rosatom.documentflow.models.UserPassport;
//import ru.rosatom.documentflow.models.UserRole;
//import ru.rosatom.documentflow.repositories.UserPassportRepository;
//import ru.rosatom.documentflow.repositories.UserRepository;
//
//import java.time.LocalDate;
//
//@SpringBootTest
//class UserServiceImplTest {
//    @Autowired
//    private UserServiceImpl userService;
//    @MockBean
//    private UserRepository userRepository;
//    @MockBean
//    private UserPassportRepository userPassportRepository;
//    private User user;
//    private UserPassport userPassport;
//    private UserOrganization userOrganization;
//
//    @BeforeEach
//    void setUp() {
//        userPassport = new UserPassport(111L, "2222", "333333", "444", LocalDate.of(2020, 02, 10), "105");
//        userOrganization = new UserOrganization();
//        user = new User(11L, "Andrey", "Andreev", "Andreevich", LocalDate.of(2000, 02, 10),
//                " qqq@mail.ru", "79262555555", "password", "post", UserRole.USER, userPassport, userOrganization);
//    }
//
//    @Test
//    void createUser() {
//        userService.createUser(userOrganization, userPassport, user);
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//        Mockito.verify(userPassportRepository, Mockito.times(1)).save(userPassport);
//    }
