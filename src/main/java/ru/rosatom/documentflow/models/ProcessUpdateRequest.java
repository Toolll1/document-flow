package ru.rosatom.documentflow.models;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ProcessUpdateRequest {
    private Long processId;
    private DocProcessComment comment;
}
