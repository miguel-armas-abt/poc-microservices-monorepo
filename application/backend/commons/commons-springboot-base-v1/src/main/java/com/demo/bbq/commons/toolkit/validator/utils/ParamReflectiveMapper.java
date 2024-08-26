package com.demo.bbq.commons.toolkit.validator.utils;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParamReflectiveMapper {

  public static <T extends DefaultParams> T mapParam(Map<String, String> params,
                                                     Class<T> paramClass,
                                                     boolean isFieldNameSensitiveCase) {
    try {
      Object paramObject = paramClass.getDeclaredConstructor().newInstance();
      populateObject(params, paramObject, isFieldNameSensitiveCase);
      return (T) paramObject;
    } catch (Exception ex) {
      throw new SystemException("ParamMappingError", ex.getMessage());
    }
  }

  private static void populateObject(Map<String, String> params,
                                     Object paramObject,
                                     boolean isFieldNameSensitiveCase) {

    Class<?> paramClass = paramObject.getClass();
    List<Field> fields = new ArrayList<>();

    while (paramClass != null) {
      fields.addAll(Arrays.asList(paramClass.getDeclaredFields()));
      paramClass = paramClass.getSuperclass();
    }

    Map<String, String> processedParams = isFieldNameSensitiveCase
        ? convertKeysToLowerCase.apply(params)
        : params;

    fields.stream()
        .peek(field -> field.setAccessible(true))
        .forEach(field -> {
          ParamName paramNameAnnotation = field.getAnnotation(ParamName.class);
          String paramName = Optional.ofNullable(paramNameAnnotation)
              .map(ParamName::value)
              .orElseGet(field::getName);

          if(isFieldNameSensitiveCase)
            paramName = paramName.toLowerCase();

          Optional.ofNullable(processedParams.get(paramName))
              .ifPresent(paramValue -> {
                try {
                  field.set(paramObject, paramValue);
                } catch (IllegalAccessException ex) {
                  throw new SystemException("ParamAssignmentError", ex.getMessage());
                }
              });
        });
  }

  private static final UnaryOperator<Map<String, String>> convertKeysToLowerCase = params ->
      params.entrySet().stream()
          .collect(Collectors.toMap(
              entry -> entry.getKey().toLowerCase(),
              Map.Entry::getValue
          ));
}