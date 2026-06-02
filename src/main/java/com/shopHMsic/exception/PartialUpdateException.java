package com.shopHMsic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PartialUpdateException extends Exception {

    private Object details;

    public PartialUpdateException(String message) {
        super(message);
    }

    public PartialUpdateException(String message, Object details) {
        super(message);
        this.details = details;
    }

    public Object getDetails() {
        return details;
    }
}
