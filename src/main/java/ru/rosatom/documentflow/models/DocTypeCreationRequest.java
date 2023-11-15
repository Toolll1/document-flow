package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocTypeCreationRequest {
    private String name;
}
