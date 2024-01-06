package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Schema(description = "Данные пользователя для авторизации")
public class UserCredentialsDto {
    @Schema(description = "Email", requiredMode = REQUIRED)
    private final String email;
    @Schema(description = "Пароль", requiredMode = REQUIRED)
    private final String password;
}
