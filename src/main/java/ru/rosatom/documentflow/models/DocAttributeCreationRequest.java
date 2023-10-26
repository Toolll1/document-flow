package ru.rosatom.documentflow.models;

import lombok.Data;

@Data
public class DocAttributeCreationRequest {
    private String name;
    private String type;
}
