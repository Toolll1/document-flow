
package ru.rosatom.e2e.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
public class AttributeSearchResponse {

    @JsonProperty("content")
    private List<Attribute> attributes;


}
