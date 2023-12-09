package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocAttributesSecurityService {

    private final String ORG_SORT = "userOrganization";

//    public boolean hasAccessToAttributes(User user, Pageable pageable) {
//        pageable.getSort().getOrderFor("")   }


}
