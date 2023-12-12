package ru.rosatom.documentflow.models;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    USER,
    ADMIN,
    ADMINCOMPANY;

    @Override
    public String getAuthority() {
        return name();
    }
}
