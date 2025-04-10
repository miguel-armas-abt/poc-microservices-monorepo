package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import lombok.Getter;

@Getter
public class NoSuchLoggingTemplateException extends GenericException {

    public NoSuchLoggingTemplateException() {
        super(ErrorDictionary.NO_SUCH_OBFUSCATION_TEMPLATE.getMessage(), ErrorDictionary.parse(NoSuchLoggingTemplateException.class));
    }
}
