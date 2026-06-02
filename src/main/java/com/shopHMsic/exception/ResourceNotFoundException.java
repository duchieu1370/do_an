package com.shopHMsic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends IOException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
