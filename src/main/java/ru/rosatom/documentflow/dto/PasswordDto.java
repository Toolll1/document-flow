package ru.rosatom.documentflow.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
@Data
public class PasswordDto {
    @Valid
    @Size(min = 8, message = "password is too short")
    private String password;
}
