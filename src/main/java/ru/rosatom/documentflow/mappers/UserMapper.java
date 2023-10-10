package ru.rosatom.documentflow.mappers;

import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.adapters.DateTimeAdapter;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.models.UserRole;

@Service
public class UserMapper {

    public UserReplyDto objectToReplyDto(User user) {

        return UserReplyDto.builder()
                .id(user.getId())
                .fullName(createFullName(user))
                .dateOfBird(DateTimeAdapter.dateToString(user.getDateOfBirth()))
                .phone(user.getPhone())
                .email(user.getEmail())
                .post(user.getPost())
                .role(user.getRole())
                .passport(user.getPassport())
                .organization(user.getOrganization())
                .build();
    }

    public User dtoToObject(UserCreateDto dto, UserOrganization organization, UserPassport passport) {

        return User.builder()
                .organization(organization)
                .passport(passport)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patronymic(dto.getPatronymic())
                .dateOfBirth(DateTimeAdapter.stringToDate(dto.getDateOfBird()))
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .post(dto.getPost())
                .role(UserRole.valueOf(dto.getRole()))
                .build();
    }

    private String createFullName(User user) {

        StringBuilder fullName = new StringBuilder();

        fullName.append(user.getFirstName());

        if (user.getPatronymic() != null) {
            fullName.append(" ").append(user.getPatronymic());
        }

        fullName.append(" ").append(user.getLastName());

        return fullName.toString();
    }
}
