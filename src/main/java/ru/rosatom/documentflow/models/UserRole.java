package ru.rosatom.documentflow.models;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    USER,
    ADMIN,
    COMPANY_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
