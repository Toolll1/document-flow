
package ru.rosatom.e2e.attribute;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.organization.Organization;

@Data
@NoArgsConstructor
public class Attribute {



    private Integer id;


    private String name;


    private String type;


    private Organization organization;


}
