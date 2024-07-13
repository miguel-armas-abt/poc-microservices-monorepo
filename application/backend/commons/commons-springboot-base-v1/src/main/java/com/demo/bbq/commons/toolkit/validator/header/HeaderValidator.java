package com.demo.bbq.commons.toolkit.validator.header;

import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.toolkit.validator.RequestValidator;
import java.lang.reflect.Field;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeaderValidator {

    private final RequestValidator requestValidator;

    public  <T extends DefaultHeaders> void validateHeaders(Map<String, String> headers, Class<T> headerClass) {
        try {
            Object headerObject = headerClass.getDeclaredConstructor().newInstance();
            populateHeaderObject(headerObject, headers);
            requestValidator.validateRequest(headerObject);
        } catch (Exception ex) {
            throw new BusinessException("HeaderValidationError", ex.getMessage());
        }
    }

    private void populateHeaderObject(Object headerObject, Map<String, String> headers) {
        Class<?> headerClass = headerObject.getClass();
        List<Field> fields = new ArrayList<>();

        while (headerClass != null) {
            fields.addAll(Arrays.asList(headerClass.getDeclaredFields()));
            headerClass = headerClass.getSuperclass();
        }

        fields.stream()
            .peek(field -> field.setAccessible(true))
            .forEach(field -> {
                HeaderName headerNameAnnotation = field.getAnnotation(HeaderName.class);
                String headerName = Optional.ofNullable(headerNameAnnotation)
                    .map(HeaderName::value)
                    .orElseGet(field::getName);

                Optional.ofNullable(headers.get(headerName))
                    .ifPresent(headerValue -> {
                        try {
                            field.set(headerObject, headerValue);
                        } catch (IllegalAccessException ex) {
                            throw new SystemException("HeaderAssignmentError", ex.getMessage());
                        }
                    });
            });
    }
}