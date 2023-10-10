package ru.rosatom.documentflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.models.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class UserReplyDto {

    private final Long id;
    private final String fullName;
    private final String dateOfBird;
    private final String email;
    private final String phone;
    private final String post;
    private final UserRole role;
    private final UserPassport passport;
    private final UserOrganization organization;
}
