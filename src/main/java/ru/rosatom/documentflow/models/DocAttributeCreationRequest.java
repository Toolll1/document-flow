package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocAttributeCreationRequest {
    private String name;
    private String type;
}
