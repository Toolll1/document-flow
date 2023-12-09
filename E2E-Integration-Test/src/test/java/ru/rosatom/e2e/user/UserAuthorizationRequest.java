package ru.rosatom.e2e.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class UserAuthorizationRequest {
        private String email;
        private String password;
}
