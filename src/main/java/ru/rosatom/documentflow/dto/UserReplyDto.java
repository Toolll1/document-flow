package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Schema(description = "Ответ на получение пользователя")
public class UserReplyDto {

    @Schema(description = "ID пользователя")
    private final Long id;

    @Schema(description = "ФИО")
    private final String fullName;

    @Schema(description = "Дата рождения")
    private final String dateOfBirth;

    @Schema(description = "Email")
    private final String email;

    @Schema(description = "Телефон")
    private final String phone;

    @Schema(description = "Описание")
    private final String post;

    @Schema(description = "Роль")
    private final String role;

    @Schema(description = "Паспорт")
    private final UserPassportDto userPassportDto;

    @Schema(description = "Организация")
    private final OrgDto organization;

}
