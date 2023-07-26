package com.defectlist.inwarranty.exception;

public class ProhibitedUserTriedToLoginException extends RuntimeException {

    public ProhibitedUserTriedToLoginException(final String message) {
        super(message);
    }
}
