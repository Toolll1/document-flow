package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.models.User;

@Component
@AllArgsConstructor
public class DocAttributesSecurityService {

    private final String ORG_SORT = "userOrganization";

//    public boolean hasAccessToAttributes(User user, Pageable pageable) {
//        pageable.getSort().getOrderFor("")   }


}
