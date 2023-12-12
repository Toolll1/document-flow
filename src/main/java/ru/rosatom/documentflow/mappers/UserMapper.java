package ru.rosatom.documentflow.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.adapters.DateTimeAdapter;
import ru.rosatom.documentflow.dto.OrgDto;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserPassportDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.models.UserRole;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserReplyDto objectToReplyDto(User user) {

        return UserReplyDto.builder()
                .id(user.getId())
                .fullName(null)
                .dateOfBirth(DateTimeAdapter.dateToString(user.getDateOfBirth()))
                .phone(user.getPhone())
                .email(user.getEmail())
                .post(null)
                .role(user.getRole().toString())
                .userPassportDto(modelMapper.map(user.getPassport(), UserPassportDto.class))
                .userOrganization(modelMapper.map(user.getOrganization(), OrgDto.class))
                .build();
    }

    public User dtoToObject(UserCreateDto dto, UserOrganization organization, UserPassport passport) {

        return User.builder()
                .organization(organization)
                .passport(passport)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patronymic(dto.getPatronymic())
                .dateOfBirth(DateTimeAdapter.stringToDate(dto.getDateOfBirth()))
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
