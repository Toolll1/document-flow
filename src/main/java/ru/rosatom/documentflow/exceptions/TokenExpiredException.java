package ru.rosatom.documentflow.exceptions;

import javax.naming.AuthenticationException;

public class TokenExpiredException  extends AuthenticationException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
