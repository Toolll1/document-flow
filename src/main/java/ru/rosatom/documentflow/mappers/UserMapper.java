package ru.rosatom.documentflow.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.dto.OrgDto;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.exceptions.UserRoleNotFoundException;
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
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .patronymic(user.getPatronymic())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .email(user.getEmail())
                .post(user.getPost())
                .role(user.getRole().toString())
                .organization(modelMapper.map(user.getOrganization(), OrgDto.class))
                .build();
    }

    public User dtoToObject(UserCreateDto dto, UserOrganization organization, UserPassport passport) {
        UserRole role;
        try {
            role = UserRole.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserRoleNotFoundException("Роль пользователя '" + dto.getRole() + "' не найдена");
        }
        return User.builder()
                .organization(organization)
                .passport(passport)
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .patronymic(dto.getPatronymic())
                .dateOfBirth(dto.getDateOfBirth())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .post(dto.getPost())
                .role(role)
                .build();
    }
}
