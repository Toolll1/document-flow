package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Токен авторизации и информация о пользователе")
public class AuthTokenDto {

    @Schema(description = "JWT токен доступа", requiredMode = REQUIRED)
    private String token;

    @Schema(description = "Владелец токена", requiredMode = REQUIRED)
    private UserWithoutPassportDto user;
}
