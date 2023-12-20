package ru.rosatom.e2e.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrganizationUpdateResponse {
    private Integer id;

    private String name;

    private String inn;

}
