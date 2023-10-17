package ru.rosatom.documentflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class UserCredentialsDto {
    private final String email;
    private final String password;
}
