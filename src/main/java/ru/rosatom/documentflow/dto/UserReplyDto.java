package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Ответ на получение пользователя")
public class UserReplyDto {

    @Schema(name = "ID пользователя")
    private final Long id;

    @Schema(name = "ФИО")
    private final String fullName;

    @Schema(name = "Дата рождения")
    private final String dateOfBirth;

    @Schema(name = "Email")
    private final String email;

    @Schema(name = "Телефон")
    private final String phone;

    @Schema(name = "Описание")
    private final String post;

    @Schema(name = "Роль")
    private final UserRole role;

    @Schema(name = "Паспорт")
    private final UserPassport passport;

    @Schema(name = "Организация")
    private final UserOrganization organization;
}
