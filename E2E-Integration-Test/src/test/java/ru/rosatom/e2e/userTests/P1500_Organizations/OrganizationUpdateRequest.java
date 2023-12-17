package ru.rosatom.e2e.userTests.P1500_Organizations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
public class OrganizationUpdateRequest {

    private String name;

}


