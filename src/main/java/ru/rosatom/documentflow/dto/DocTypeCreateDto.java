package ru.rosatom.documentflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class DocTypeCreateDto {
    @Size(min = 1, max = 255)
    private String name;
    private String agreementType;
}
