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
@Schema(name = "Данные пользователя для авторизации")
public class UserCredentialsDto {
    @Schema(name = "Email")
    private final String email;
    @Schema(name = "Пароль")
    private final String password;
}
