package ru.rosatom.e2e.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.organization.Organization;

@Data
@NoArgsConstructor
public class UserWithOrg {
    private int id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String dateOfBirth;
    private String email;
    private String phone;
    private String post;
    private String role;
    private Organization organization;
}
