package com.shopHMsic.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExceptionDTOResponse {
    private String code;
    private String message;
}
