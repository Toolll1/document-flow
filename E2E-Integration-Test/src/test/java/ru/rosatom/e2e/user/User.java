package ru.rosatom.e2e.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.organization.Organization;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User{
    private Integer id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String dateOfBirth;
    private String email;
    private String phone;
    private Organization organization;
    private UserRole role;
    private String post;
}