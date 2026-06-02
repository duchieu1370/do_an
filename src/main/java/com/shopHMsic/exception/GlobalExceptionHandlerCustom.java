package com.shopHMsic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerCustom {
    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<ExceptionDTOResponse> handleValidateException(ValidateException e) {
        // Tạo đối tượng ResponseDTO với thông tin lỗi
        ExceptionDTOResponse resError = ExceptionDTOResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();
        if(e.getCode().equals("409")){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resError);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resError);
    }
}
