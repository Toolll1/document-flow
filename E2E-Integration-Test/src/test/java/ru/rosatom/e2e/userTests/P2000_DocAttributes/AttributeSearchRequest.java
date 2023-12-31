package ru.rosatom.e2e.userTests.P2000_DocAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AttributeSearchRequest {
    private Integer page;
    private Integer size;
    private List<String> sort;
    private Integer orgId;
}
