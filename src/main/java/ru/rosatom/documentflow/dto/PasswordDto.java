package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Обновление пароля")
public class PasswordDto {

    @Schema(description = "Пароль", minLength = 8, requiredMode = REQUIRED)
    @Valid
    @Size(min = 8, message = "password is too short")
    private String password;
}
