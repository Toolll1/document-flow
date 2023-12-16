package ru.rosatom.e2e.organization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
public class OrganizationSearchResponseId {

//    @JsonProperty("id")
//    private Organization organization;

    private Integer id;

    private String name;

    private String inn;
}