package ru.rosatom.documentflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class OrgCreateRequestDto {
    @Size(min = 1, max = 255)
    private String name;

    @Pattern(regexp = "\\d{10}")
    private String inn;

}
