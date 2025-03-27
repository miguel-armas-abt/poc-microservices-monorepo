package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class NoSuchObfuscationTemplateException extends GenericException {

    private static final ErrorDictionary EXCEPTION = ErrorDictionary.NO_SUCH_OBFUSCATION_TEMPLATE;

    public NoSuchObfuscationTemplateException() {
        super(EXCEPTION.getMessage());
        this.httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
        this.errorDetail = ErrorDTO.builder()
            .code(EXCEPTION.getCode())
            .message(EXCEPTION.getMessage())
            .build();
    }
}
