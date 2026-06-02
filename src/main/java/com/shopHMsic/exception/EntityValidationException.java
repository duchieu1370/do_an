package com.shopHMsic.exception;

public class EntityValidationException extends Exception {

    private Object details;

    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Object details) {
        super(message);
        this.details = details;
    }

    public Object getDetails() {
        return details;
    }
}
