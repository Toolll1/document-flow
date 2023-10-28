package ru.rosatom.documentflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcessUpdateRequestDto {
    private Long processId;
    private String comment;
}
