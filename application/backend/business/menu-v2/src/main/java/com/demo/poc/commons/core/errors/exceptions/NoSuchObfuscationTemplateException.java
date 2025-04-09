package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.enums.ErrorDictionary;
import lombok.Getter;

@Getter
public class NoSuchObfuscationTemplateException extends GenericException {

    private static final ErrorDictionary EXCEPTION = ErrorDictionary.NO_SUCH_OBFUSCATION_TEMPLATE;

    public NoSuchObfuscationTemplateException() {
        super(ErrorDictionary.NO_SUCH_OBFUSCATION_TEMPLATE.getMessage(), ErrorDictionary.parse(NoSuchObfuscationTemplateException.class));
    }
}
