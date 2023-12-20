package ru.rosatom.e2e.userTests.P1500_Organizations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrganizationAddRequest {

    private String name;

    private String inn;

}
