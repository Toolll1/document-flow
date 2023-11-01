package ru.rosatom.documentflow.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocAttributeValueCreateDto {

    @NotNull
    Long attributeId;
    @NotNull
    @NotEmpty
    String value;
}
