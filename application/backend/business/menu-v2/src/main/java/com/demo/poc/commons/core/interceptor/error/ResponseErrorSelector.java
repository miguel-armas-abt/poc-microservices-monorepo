package com.demo.poc.commons.core.interceptor.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.properties.ProjectType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.poc.commons.core.errors.dto.ErrorDto.CODE_DEFAULT;

public class ResponseErrorSelector {

  public static <T extends Throwable> ErrorDto toErrorDTO(ConfigurationBaseProperties properties, T exception) {
    ErrorDto error = extractError(exception);
    String selectedCode = selectCustomCode(properties, error);
    error.setCode(selectedCode);

    String selectedMessage = selectMessage(properties, error);
    error.setMessage(selectedMessage);

    return error;
  }

  private static String selectCustomCode(ConfigurationBaseProperties properties, ErrorDto error) {
    ProjectType projectType = selectProjectType(properties);

    return ProjectType.BFF.equals(projectType)
        ? CODE_DEFAULT
        : Optional.ofNullable(error)
        .map(ErrorDto::getCode)
        .orElseGet(() -> CODE_DEFAULT);
  }

  private static String selectMessage(ConfigurationBaseProperties properties, ErrorDto error) {
    String defaultMessage = ErrorDto.getDefaultError(properties).getMessage();
    ProjectType projectType = selectProjectType(properties);

    if(ProjectType.BFF.equals(projectType))
      return defaultMessage;

    return Optional.ofNullable(ErrorDto.getMatchMessage(properties, error.getCode()))
        .orElseGet(() -> Optional.ofNullable(error.getMessage()).orElse(defaultMessage));
  }

  private static ProjectType selectProjectType(ConfigurationBaseProperties properties) {
    return ProjectType.valueOf(properties.projectType().orElse(ProjectType.MS.name()));
  }

  private static <T extends Throwable> ErrorDto extractError(T exception) {
    ErrorDto error = null;

    if (exception instanceof GenericException genericException)
      error = genericException.getErrorDetail();

    if(exception instanceof ConstraintViolationException constraintViolationException)
      error = extractError(constraintViolationException);

    return error;
  }

  private static ErrorDto extractError(ConstraintViolationException exception) {
    String message = exception
        .getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(";"));

    return ErrorDto.builder()
        .code(ErrorDictionary.INVALID_FIELD.getCode())
        .message(message)
        .type(ErrorType.BUSINESS)
        .build();
  }
}
