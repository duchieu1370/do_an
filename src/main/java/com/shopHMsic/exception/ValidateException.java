package com.shopHMsic.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ValidateException extends RuntimeException{
    String code;
    String message;

    public ValidateException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
