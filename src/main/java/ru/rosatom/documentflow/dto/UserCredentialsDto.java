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
@Schema(description = "Данные пользователя для авторизации")
public class UserCredentialsDto {
    @Schema(description = "Email")
    private final String email;
    @Schema(description = "Пароль")
    private final String password;
}
