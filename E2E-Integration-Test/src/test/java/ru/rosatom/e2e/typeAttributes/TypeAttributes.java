package ru.rosatom.e2e.typeAttributes;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.attribute.Attribute;
import ru.rosatom.e2e.docType.DocType;

@Data
@NoArgsConstructor
public class TypeAttributes {
    private DocType docType;
    private Attribute attribute;
}
