package ru.rosatom.documentflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuthTokenDto {

    private String token;

    private UserWithoutPassportDto userWithoutPassport;
}
