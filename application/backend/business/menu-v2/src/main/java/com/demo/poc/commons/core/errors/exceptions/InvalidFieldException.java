package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class InvalidFieldException extends GenericException {

    private static final ErrorDictionary EXCEPTION = ErrorDictionary.INVALID_FIELD;

    public InvalidFieldException(String message) {
        super(message);
        this.httpStatus = Response.Status.BAD_REQUEST;
        this.errorDetail = ErrorDTO.builder()
            .code(EXCEPTION.getCode())
            .message(EXCEPTION.getMessage())
            .build();
    }
}
