package ru.rosatom.documentflow.models;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrgCreationRequest {
    private String name;
    private String inn;
}
