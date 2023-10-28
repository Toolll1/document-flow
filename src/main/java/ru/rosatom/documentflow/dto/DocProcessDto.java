package ru.rosatom.documentflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class DocProcessDto {

    private Long id;
    private Long document;
    private Long sender;
    private Long recipient;
    private String  status;
    private String comment;
}
