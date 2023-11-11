package ru.rosatom.documentflow.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentCreateDto {

    @NotNull
    Long idOrganization;
    @NotNull
    Long docTypId;
    @NotNull
    List<DocAttributeValueCreateDto> docAttributeValueCreateDtos;
}
