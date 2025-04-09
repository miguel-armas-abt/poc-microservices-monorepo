package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException {

    protected ErrorDTO errorDetail;
    protected HttpStatus httpStatus;

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, ErrorDictionary detail) {
        super(message);
        this.httpStatus = detail.getHttpStatus();
        this.errorDetail = ErrorDTO.builder()
                .type(detail.getType())
                .code(detail.getCode())
                .message(detail.getMessage())
                .build();
    }
}
