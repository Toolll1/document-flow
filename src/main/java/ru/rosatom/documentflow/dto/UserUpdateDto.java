package ru.rosatom.documentflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    private final Long id;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 100)
    private final String lastName;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 100)
    private final String firstName;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 100)
    private final String patronymic;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 10, max = 10)
    private final String dateOfBirth;
    @Nullable
    @Email
    @Size(min = 6, max = 320)
    private final String email;
    @Nullable
    @Pattern(regexp = "\\d+")
    @Size(min = 11, max = 11)
    private final String phone;
    @Nullable
    @Pattern(regexp = "\\d+")
    @Size(min = 4, max = 4)
    private final String passportSeries;
    @Nullable
    @Pattern(regexp = "\\d+")
    @Size(min = 6, max = 6)
    private final String passportNumber;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 2, max = 1000)
    private final String passportIssued; //кем выдан
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 10, max = 10)
    private final String passportDate;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 6, max = 6)
    private final String passportKp;  //код подразделения
    @Nullable
    private final Long organizationId;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    private final String role;
    @Nullable
    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    @Size(min = 1, max = 320)
    private String post;
}
