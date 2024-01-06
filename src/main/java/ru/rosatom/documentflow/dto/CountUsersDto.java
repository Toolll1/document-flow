package ru.rosatom.documentflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Schema(description = "Кол-во пользователей")
public class CountUsersDto {

    @Schema(description = "Количество пользователей", requiredMode = REQUIRED)
    private final int countUser;
}
