package ru.rosatom.documentflow.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocAttributeUpdateRequest {
    private String name;
    private String type;
}
