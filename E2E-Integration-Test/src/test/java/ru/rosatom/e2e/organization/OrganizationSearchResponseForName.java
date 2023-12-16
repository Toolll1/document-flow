package ru.rosatom.e2e.organization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class OrganizationSearchResponseForName {
    @JsonProperty("content")
    private Organization[] organizations;

}
