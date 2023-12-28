package ru.rosatom.e2e.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.organization.Organization;

@Data
@NoArgsConstructor
public class UserWithPassportAndOrg {
    private int id;
    private String fullName;
    private String dateOfBirth;
    private String email;
    private String phone;
    private String post;
    private String role;
    private Passport userPassportDto;
    private Organization organization;
}
