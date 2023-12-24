package ru.rosatom.e2e.attributeValues;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.attribute.Attribute;
import ru.rosatom.e2e.document.Document;

@Data
@NoArgsConstructor
public class AttributeValues {
    private Integer valueId;
    private String value;
    private Attribute attribute;
    private Document document;
}
