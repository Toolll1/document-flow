package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor(force = true)
@Data
@AllArgsConstructor
public class ProcessUpdateRequest {
    private Long processId;
    private String comment;
}
