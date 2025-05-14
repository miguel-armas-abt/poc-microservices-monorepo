package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorOrigin;
import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {

    protected ErrorDto errorDetail;
    protected Response.Status httpStatus;

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, ErrorDictionary detail) {
        super(message);
        this.httpStatus = detail.getHttpStatus();
        this.errorDetail = ErrorDto.builder()
            .origin(ErrorOrigin.OWN)
            .code(detail.getCode())
            .message(detail.getMessage())
            .build();
    }
}
