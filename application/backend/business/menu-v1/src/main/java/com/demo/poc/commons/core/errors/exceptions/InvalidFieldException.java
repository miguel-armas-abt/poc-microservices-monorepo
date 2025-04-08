package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;

@Getter
public class InvalidFieldException extends GenericException {

    public InvalidFieldException(String message) {
        super(message);

        ErrorDictionary detail = ErrorDictionary.parse(this.getClass());
        this.httpStatus = detail.getHttpStatus();
        this.errorDetail = ErrorDTO.builder()
                .type(detail.getType())
                .code(detail.getCode())
                .message(detail.getMessage())
                .build();
    }
}
