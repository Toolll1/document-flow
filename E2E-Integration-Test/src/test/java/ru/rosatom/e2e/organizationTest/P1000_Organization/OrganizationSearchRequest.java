package ru.rosatom.e2e.organizationTest.P1000_Organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrganizationSearchRequest {
    private Integer id;

    private String name;

    private String inn;

}
