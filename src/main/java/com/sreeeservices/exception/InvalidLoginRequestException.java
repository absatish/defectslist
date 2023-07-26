package com.sreeeservices.exception;

import javax.naming.AuthenticationException;

public class InvalidLoginRequestException extends AuthenticationException {

    public InvalidLoginRequestException(final String message) {
        super(message);
    }
}
